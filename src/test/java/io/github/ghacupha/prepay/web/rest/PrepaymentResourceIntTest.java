package io.github.ghacupha.prepay.web.rest;

import io.github.ghacupha.prepay.PrepayApp;

import io.github.ghacupha.prepay.domain.Prepayment;
import io.github.ghacupha.prepay.repository.PrepaymentRepository;
import io.github.ghacupha.prepay.repository.search.PrepaymentSearchRepository;
import io.github.ghacupha.prepay.service.IPrepaymentService;
import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;
import io.github.ghacupha.prepay.service.mapper.PrepaymentMapper;
import io.github.ghacupha.prepay.web.rest.errors.ExceptionTranslator;
import io.github.ghacupha.prepay.service.PrepaymentQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static io.github.ghacupha.prepay.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PrepaymentResource REST controller.
 *
 * @see PrepaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PrepayApp.class)
public class PrepaymentResourceIntTest {

    private static final String DEFAULT_PREPAYMENT_ACCOUNT = "AAAAAAAAAAAAA";
    private static final String UPDATED_PREPAYMENT_ACCOUNT = "BBBBBBBBBBBBB";

    private static final String DEFAULT_PREPAYMENT_ACCOUNT_NUMBER = "AAAAAAAAAAAAA";
    private static final String UPDATED_PREPAYMENT_ACCOUNT_NUMBER = "BBBBBBBBBBBBB";

    private static final String DEFAULT_NARRATION = "AAAAAAAAAA";
    private static final String UPDATED_NARRATION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_OUTLET_CODE = "AAA";
    private static final String UPDATED_SERVICE_OUTLET_CODE = "BBB";

    private static final String DEFAULT_EXPENSE_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_ACCOUNT_NUMBER = "AAAAAAAAAAAAA";
    private static final String UPDATED_EXPENSE_ACCOUNT_NUMBER = "BBBBBBBBBBBBB";

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_PREPAYMENT_TERM = 1;
    private static final Integer UPDATED_PREPAYMENT_TERM = 2;

    @Autowired
    private PrepaymentRepository prepaymentRepository;

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @Autowired
    private IPrepaymentService IPrepaymentService;

    /**
     * This repository is mocked in the io.github.ghacupha.prepay.repository.search test package.
     *
     * @see io.github.ghacupha.prepay.repository.search.PrepaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentSearchRepository mockPrepaymentSearchRepository;

    @Autowired
    private PrepaymentQueryService prepaymentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPrepaymentMockMvc;

    private Prepayment prepayment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrepaymentResource prepaymentResource = new PrepaymentResource(IPrepaymentService, prepaymentQueryService);
        this.restPrepaymentMockMvc = MockMvcBuilders.standaloneSetup(prepaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prepayment createEntity(EntityManager em) {
        Prepayment prepayment = new Prepayment()
            .prepaymentAccount(DEFAULT_PREPAYMENT_ACCOUNT)
            .prepaymentAccountNumber(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER)
            .narration(DEFAULT_NARRATION)
            .remarks(DEFAULT_REMARKS)
            .serviceOutletCode(DEFAULT_SERVICE_OUTLET_CODE)
            .expenseAccount(DEFAULT_EXPENSE_ACCOUNT)
            .expenseAccountNumber(DEFAULT_EXPENSE_ACCOUNT_NUMBER)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .amount(DEFAULT_AMOUNT)
            .prepaymentTerm(DEFAULT_PREPAYMENT_TERM);
        return prepayment;
    }

    @Before
    public void initTest() {
        prepayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrepayment() throws Exception {
        int databaseSizeBeforeCreate = prepaymentRepository.findAll().size();

        // Create the Prepayment
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);
        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Prepayment in the database
        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeCreate + 1);
        Prepayment testPrepayment = prepaymentList.get(prepaymentList.size() - 1);
        assertThat(testPrepayment.getPrepaymentAccount()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT);
        assertThat(testPrepayment.getPrepaymentAccountNumber()).isEqualTo(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER);
        assertThat(testPrepayment.getNarration()).isEqualTo(DEFAULT_NARRATION);
        assertThat(testPrepayment.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testPrepayment.getServiceOutletCode()).isEqualTo(DEFAULT_SERVICE_OUTLET_CODE);
        assertThat(testPrepayment.getExpenseAccount()).isEqualTo(DEFAULT_EXPENSE_ACCOUNT);
        assertThat(testPrepayment.getExpenseAccountNumber()).isEqualTo(DEFAULT_EXPENSE_ACCOUNT_NUMBER);
        assertThat(testPrepayment.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testPrepayment.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testPrepayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPrepayment.getPrepaymentTerm()).isEqualTo(DEFAULT_PREPAYMENT_TERM);

        // Validate the Prepayment in Elasticsearch
        verify(mockPrepaymentSearchRepository, times(1)).save(testPrepayment);
    }

    @Test
    @Transactional
    public void createPrepaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prepaymentRepository.findAll().size();

        // Create the Prepayment with an existing ID
        prepayment.setId(1L);
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prepayment in the database
        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Prepayment in Elasticsearch
        verify(mockPrepaymentSearchRepository, times(0)).save(prepayment);
    }

    @Test
    @Transactional
    public void checkPrepaymentAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setPrepaymentAccount(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrepaymentAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setPrepaymentAccountNumber(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNarrationIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setNarration(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServiceOutletCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setServiceOutletCode(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpenseAccountIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setExpenseAccount(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpenseAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setExpenseAccountNumber(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setTransactionId(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setTransactionDate(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setAmount(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrepaymentTermIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentRepository.findAll().size();
        // set the field null
        prepayment.setPrepaymentTerm(null);

        // Create the Prepayment, which fails.
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        restPrepaymentMockMvc.perform(post("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrepayments() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList
        restPrepaymentMockMvc.perform(get("/api/prepayments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccount").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAccountNumber").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].narration").value(hasItem(DEFAULT_NARRATION.toString())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE.toString())))
            .andExpect(jsonPath("$.[*].expenseAccount").value(hasItem(DEFAULT_EXPENSE_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].expenseAccountNumber").value(hasItem(DEFAULT_EXPENSE_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].prepaymentTerm").value(hasItem(DEFAULT_PREPAYMENT_TERM)));
    }

    @Test
    @Transactional
    public void getPrepayment() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get the prepayment
        restPrepaymentMockMvc.perform(get("/api/prepayments/{id}", prepayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prepayment.getId().intValue()))
            .andExpect(jsonPath("$.prepaymentAccount").value(DEFAULT_PREPAYMENT_ACCOUNT.toString()))
            .andExpect(jsonPath("$.prepaymentAccountNumber").value(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.narration").value(DEFAULT_NARRATION.toString()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()))
            .andExpect(jsonPath("$.serviceOutletCode").value(DEFAULT_SERVICE_OUTLET_CODE.toString()))
            .andExpect(jsonPath("$.expenseAccount").value(DEFAULT_EXPENSE_ACCOUNT.toString()))
            .andExpect(jsonPath("$.expenseAccountNumber").value(DEFAULT_EXPENSE_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.prepaymentTerm").value(DEFAULT_PREPAYMENT_TERM));
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccount equals to DEFAULT_PREPAYMENT_ACCOUNT
        defaultPrepaymentShouldBeFound("prepaymentAccount.equals=" + DEFAULT_PREPAYMENT_ACCOUNT);

        // Get all the prepaymentList where prepaymentAccount equals to UPDATED_PREPAYMENT_ACCOUNT
        defaultPrepaymentShouldNotBeFound("prepaymentAccount.equals=" + UPDATED_PREPAYMENT_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccount in DEFAULT_PREPAYMENT_ACCOUNT or UPDATED_PREPAYMENT_ACCOUNT
        defaultPrepaymentShouldBeFound("prepaymentAccount.in=" + DEFAULT_PREPAYMENT_ACCOUNT + "," + UPDATED_PREPAYMENT_ACCOUNT);

        // Get all the prepaymentList where prepaymentAccount equals to UPDATED_PREPAYMENT_ACCOUNT
        defaultPrepaymentShouldNotBeFound("prepaymentAccount.in=" + UPDATED_PREPAYMENT_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccount is not null
        defaultPrepaymentShouldBeFound("prepaymentAccount.specified=true");

        // Get all the prepaymentList where prepaymentAccount is null
        defaultPrepaymentShouldNotBeFound("prepaymentAccount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccountNumber equals to DEFAULT_PREPAYMENT_ACCOUNT_NUMBER
        defaultPrepaymentShouldBeFound("prepaymentAccountNumber.equals=" + DEFAULT_PREPAYMENT_ACCOUNT_NUMBER);

        // Get all the prepaymentList where prepaymentAccountNumber equals to UPDATED_PREPAYMENT_ACCOUNT_NUMBER
        defaultPrepaymentShouldNotBeFound("prepaymentAccountNumber.equals=" + UPDATED_PREPAYMENT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccountNumber in DEFAULT_PREPAYMENT_ACCOUNT_NUMBER or UPDATED_PREPAYMENT_ACCOUNT_NUMBER
        defaultPrepaymentShouldBeFound("prepaymentAccountNumber.in=" + DEFAULT_PREPAYMENT_ACCOUNT_NUMBER + "," + UPDATED_PREPAYMENT_ACCOUNT_NUMBER);

        // Get all the prepaymentList where prepaymentAccountNumber equals to UPDATED_PREPAYMENT_ACCOUNT_NUMBER
        defaultPrepaymentShouldNotBeFound("prepaymentAccountNumber.in=" + UPDATED_PREPAYMENT_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentAccountNumber is not null
        defaultPrepaymentShouldBeFound("prepaymentAccountNumber.specified=true");

        // Get all the prepaymentList where prepaymentAccountNumber is null
        defaultPrepaymentShouldNotBeFound("prepaymentAccountNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByNarrationIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where narration equals to DEFAULT_NARRATION
        defaultPrepaymentShouldBeFound("narration.equals=" + DEFAULT_NARRATION);

        // Get all the prepaymentList where narration equals to UPDATED_NARRATION
        defaultPrepaymentShouldNotBeFound("narration.equals=" + UPDATED_NARRATION);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByNarrationIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where narration in DEFAULT_NARRATION or UPDATED_NARRATION
        defaultPrepaymentShouldBeFound("narration.in=" + DEFAULT_NARRATION + "," + UPDATED_NARRATION);

        // Get all the prepaymentList where narration equals to UPDATED_NARRATION
        defaultPrepaymentShouldNotBeFound("narration.in=" + UPDATED_NARRATION);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByNarrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where narration is not null
        defaultPrepaymentShouldBeFound("narration.specified=true");

        // Get all the prepaymentList where narration is null
        defaultPrepaymentShouldNotBeFound("narration.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByRemarksIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where remarks equals to DEFAULT_REMARKS
        defaultPrepaymentShouldBeFound("remarks.equals=" + DEFAULT_REMARKS);

        // Get all the prepaymentList where remarks equals to UPDATED_REMARKS
        defaultPrepaymentShouldNotBeFound("remarks.equals=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByRemarksIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where remarks in DEFAULT_REMARKS or UPDATED_REMARKS
        defaultPrepaymentShouldBeFound("remarks.in=" + DEFAULT_REMARKS + "," + UPDATED_REMARKS);

        // Get all the prepaymentList where remarks equals to UPDATED_REMARKS
        defaultPrepaymentShouldNotBeFound("remarks.in=" + UPDATED_REMARKS);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByRemarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where remarks is not null
        defaultPrepaymentShouldBeFound("remarks.specified=true");

        // Get all the prepaymentList where remarks is null
        defaultPrepaymentShouldNotBeFound("remarks.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByServiceOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where serviceOutletCode equals to DEFAULT_SERVICE_OUTLET_CODE
        defaultPrepaymentShouldBeFound("serviceOutletCode.equals=" + DEFAULT_SERVICE_OUTLET_CODE);

        // Get all the prepaymentList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultPrepaymentShouldNotBeFound("serviceOutletCode.equals=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByServiceOutletCodeIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where serviceOutletCode in DEFAULT_SERVICE_OUTLET_CODE or UPDATED_SERVICE_OUTLET_CODE
        defaultPrepaymentShouldBeFound("serviceOutletCode.in=" + DEFAULT_SERVICE_OUTLET_CODE + "," + UPDATED_SERVICE_OUTLET_CODE);

        // Get all the prepaymentList where serviceOutletCode equals to UPDATED_SERVICE_OUTLET_CODE
        defaultPrepaymentShouldNotBeFound("serviceOutletCode.in=" + UPDATED_SERVICE_OUTLET_CODE);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByServiceOutletCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where serviceOutletCode is not null
        defaultPrepaymentShouldBeFound("serviceOutletCode.specified=true");

        // Get all the prepaymentList where serviceOutletCode is null
        defaultPrepaymentShouldNotBeFound("serviceOutletCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccount equals to DEFAULT_EXPENSE_ACCOUNT
        defaultPrepaymentShouldBeFound("expenseAccount.equals=" + DEFAULT_EXPENSE_ACCOUNT);

        // Get all the prepaymentList where expenseAccount equals to UPDATED_EXPENSE_ACCOUNT
        defaultPrepaymentShouldNotBeFound("expenseAccount.equals=" + UPDATED_EXPENSE_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccount in DEFAULT_EXPENSE_ACCOUNT or UPDATED_EXPENSE_ACCOUNT
        defaultPrepaymentShouldBeFound("expenseAccount.in=" + DEFAULT_EXPENSE_ACCOUNT + "," + UPDATED_EXPENSE_ACCOUNT);

        // Get all the prepaymentList where expenseAccount equals to UPDATED_EXPENSE_ACCOUNT
        defaultPrepaymentShouldNotBeFound("expenseAccount.in=" + UPDATED_EXPENSE_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccount is not null
        defaultPrepaymentShouldBeFound("expenseAccount.specified=true");

        // Get all the prepaymentList where expenseAccount is null
        defaultPrepaymentShouldNotBeFound("expenseAccount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccountNumber equals to DEFAULT_EXPENSE_ACCOUNT_NUMBER
        defaultPrepaymentShouldBeFound("expenseAccountNumber.equals=" + DEFAULT_EXPENSE_ACCOUNT_NUMBER);

        // Get all the prepaymentList where expenseAccountNumber equals to UPDATED_EXPENSE_ACCOUNT_NUMBER
        defaultPrepaymentShouldNotBeFound("expenseAccountNumber.equals=" + UPDATED_EXPENSE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccountNumber in DEFAULT_EXPENSE_ACCOUNT_NUMBER or UPDATED_EXPENSE_ACCOUNT_NUMBER
        defaultPrepaymentShouldBeFound("expenseAccountNumber.in=" + DEFAULT_EXPENSE_ACCOUNT_NUMBER + "," + UPDATED_EXPENSE_ACCOUNT_NUMBER);

        // Get all the prepaymentList where expenseAccountNumber equals to UPDATED_EXPENSE_ACCOUNT_NUMBER
        defaultPrepaymentShouldNotBeFound("expenseAccountNumber.in=" + UPDATED_EXPENSE_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByExpenseAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where expenseAccountNumber is not null
        defaultPrepaymentShouldBeFound("expenseAccountNumber.specified=true");

        // Get all the prepaymentList where expenseAccountNumber is null
        defaultPrepaymentShouldNotBeFound("expenseAccountNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionId equals to DEFAULT_TRANSACTION_ID
        defaultPrepaymentShouldBeFound("transactionId.equals=" + DEFAULT_TRANSACTION_ID);

        // Get all the prepaymentList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultPrepaymentShouldNotBeFound("transactionId.equals=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionIdIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionId in DEFAULT_TRANSACTION_ID or UPDATED_TRANSACTION_ID
        defaultPrepaymentShouldBeFound("transactionId.in=" + DEFAULT_TRANSACTION_ID + "," + UPDATED_TRANSACTION_ID);

        // Get all the prepaymentList where transactionId equals to UPDATED_TRANSACTION_ID
        defaultPrepaymentShouldNotBeFound("transactionId.in=" + UPDATED_TRANSACTION_ID);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionId is not null
        defaultPrepaymentShouldBeFound("transactionId.specified=true");

        // Get all the prepaymentList where transactionId is null
        defaultPrepaymentShouldNotBeFound("transactionId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultPrepaymentShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the prepaymentList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPrepaymentShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultPrepaymentShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the prepaymentList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultPrepaymentShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionDate is not null
        defaultPrepaymentShouldBeFound("transactionDate.specified=true");

        // Get all the prepaymentList where transactionDate is null
        defaultPrepaymentShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionDate greater than or equals to DEFAULT_TRANSACTION_DATE
        defaultPrepaymentShouldBeFound("transactionDate.greaterOrEqualThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the prepaymentList where transactionDate greater than or equals to UPDATED_TRANSACTION_DATE
        defaultPrepaymentShouldNotBeFound("transactionDate.greaterOrEqualThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where transactionDate less than or equals to DEFAULT_TRANSACTION_DATE
        defaultPrepaymentShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the prepaymentList where transactionDate less than or equals to UPDATED_TRANSACTION_DATE
        defaultPrepaymentShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }


    @Test
    @Transactional
    public void getAllPrepaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where amount equals to DEFAULT_AMOUNT
        defaultPrepaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the prepaymentList where amount equals to UPDATED_AMOUNT
        defaultPrepaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPrepaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the prepaymentList where amount equals to UPDATED_AMOUNT
        defaultPrepaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where amount is not null
        defaultPrepaymentShouldBeFound("amount.specified=true");

        // Get all the prepaymentList where amount is null
        defaultPrepaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentTermIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentTerm equals to DEFAULT_PREPAYMENT_TERM
        defaultPrepaymentShouldBeFound("prepaymentTerm.equals=" + DEFAULT_PREPAYMENT_TERM);

        // Get all the prepaymentList where prepaymentTerm equals to UPDATED_PREPAYMENT_TERM
        defaultPrepaymentShouldNotBeFound("prepaymentTerm.equals=" + UPDATED_PREPAYMENT_TERM);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentTermIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentTerm in DEFAULT_PREPAYMENT_TERM or UPDATED_PREPAYMENT_TERM
        defaultPrepaymentShouldBeFound("prepaymentTerm.in=" + DEFAULT_PREPAYMENT_TERM + "," + UPDATED_PREPAYMENT_TERM);

        // Get all the prepaymentList where prepaymentTerm equals to UPDATED_PREPAYMENT_TERM
        defaultPrepaymentShouldNotBeFound("prepaymentTerm.in=" + UPDATED_PREPAYMENT_TERM);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentTermIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentTerm is not null
        defaultPrepaymentShouldBeFound("prepaymentTerm.specified=true");

        // Get all the prepaymentList where prepaymentTerm is null
        defaultPrepaymentShouldNotBeFound("prepaymentTerm.specified=false");
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentTermIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentTerm greater than or equals to DEFAULT_PREPAYMENT_TERM
        defaultPrepaymentShouldBeFound("prepaymentTerm.greaterOrEqualThan=" + DEFAULT_PREPAYMENT_TERM);

        // Get all the prepaymentList where prepaymentTerm greater than or equals to UPDATED_PREPAYMENT_TERM
        defaultPrepaymentShouldNotBeFound("prepaymentTerm.greaterOrEqualThan=" + UPDATED_PREPAYMENT_TERM);
    }

    @Test
    @Transactional
    public void getAllPrepaymentsByPrepaymentTermIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        // Get all the prepaymentList where prepaymentTerm less than or equals to DEFAULT_PREPAYMENT_TERM
        defaultPrepaymentShouldNotBeFound("prepaymentTerm.lessThan=" + DEFAULT_PREPAYMENT_TERM);

        // Get all the prepaymentList where prepaymentTerm less than or equals to UPDATED_PREPAYMENT_TERM
        defaultPrepaymentShouldBeFound("prepaymentTerm.lessThan=" + UPDATED_PREPAYMENT_TERM);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPrepaymentShouldBeFound(String filter) throws Exception {
        restPrepaymentMockMvc.perform(get("/api/prepayments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccount").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].prepaymentAccountNumber").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].narration").value(hasItem(DEFAULT_NARRATION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].expenseAccount").value(hasItem(DEFAULT_EXPENSE_ACCOUNT)))
            .andExpect(jsonPath("$.[*].expenseAccountNumber").value(hasItem(DEFAULT_EXPENSE_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].prepaymentTerm").value(hasItem(DEFAULT_PREPAYMENT_TERM)));

        // Check, that the count call also returns 1
        restPrepaymentMockMvc.perform(get("/api/prepayments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPrepaymentShouldNotBeFound(String filter) throws Exception {
        restPrepaymentMockMvc.perform(get("/api/prepayments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentMockMvc.perform(get("/api/prepayments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPrepayment() throws Exception {
        // Get the prepayment
        restPrepaymentMockMvc.perform(get("/api/prepayments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrepayment() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        int databaseSizeBeforeUpdate = prepaymentRepository.findAll().size();

        // Update the prepayment
        Prepayment updatedPrepayment = prepaymentRepository.findById(prepayment.getId()).get();
        // Disconnect from session so that the updates on updatedPrepayment are not directly saved in db
        em.detach(updatedPrepayment);
        updatedPrepayment
            .prepaymentAccount(UPDATED_PREPAYMENT_ACCOUNT)
            .prepaymentAccountNumber(UPDATED_PREPAYMENT_ACCOUNT_NUMBER)
            .narration(UPDATED_NARRATION)
            .remarks(UPDATED_REMARKS)
            .serviceOutletCode(UPDATED_SERVICE_OUTLET_CODE)
            .expenseAccount(UPDATED_EXPENSE_ACCOUNT)
            .expenseAccountNumber(UPDATED_EXPENSE_ACCOUNT_NUMBER)
            .transactionId(UPDATED_TRANSACTION_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .amount(UPDATED_AMOUNT)
            .prepaymentTerm(UPDATED_PREPAYMENT_TERM);
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(updatedPrepayment);

        restPrepaymentMockMvc.perform(put("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isOk());

        // Validate the Prepayment in the database
        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeUpdate);
        Prepayment testPrepayment = prepaymentList.get(prepaymentList.size() - 1);
        assertThat(testPrepayment.getPrepaymentAccount()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT);
        assertThat(testPrepayment.getPrepaymentAccountNumber()).isEqualTo(UPDATED_PREPAYMENT_ACCOUNT_NUMBER);
        assertThat(testPrepayment.getNarration()).isEqualTo(UPDATED_NARRATION);
        assertThat(testPrepayment.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testPrepayment.getServiceOutletCode()).isEqualTo(UPDATED_SERVICE_OUTLET_CODE);
        assertThat(testPrepayment.getExpenseAccount()).isEqualTo(UPDATED_EXPENSE_ACCOUNT);
        assertThat(testPrepayment.getExpenseAccountNumber()).isEqualTo(UPDATED_EXPENSE_ACCOUNT_NUMBER);
        assertThat(testPrepayment.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testPrepayment.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testPrepayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPrepayment.getPrepaymentTerm()).isEqualTo(UPDATED_PREPAYMENT_TERM);

        // Validate the Prepayment in Elasticsearch
        verify(mockPrepaymentSearchRepository, times(1)).save(testPrepayment);
    }

    @Test
    @Transactional
    public void updateNonExistingPrepayment() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentRepository.findAll().size();

        // Create the Prepayment
        PrepaymentDTO prepaymentDTO = prepaymentMapper.toDto(prepayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentMockMvc.perform(put("/api/prepayments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prepaymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Prepayment in the database
        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Prepayment in Elasticsearch
        verify(mockPrepaymentSearchRepository, times(0)).save(prepayment);
    }

    @Test
    @Transactional
    public void deletePrepayment() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);

        int databaseSizeBeforeDelete = prepaymentRepository.findAll().size();

        // Delete the prepayment
        restPrepaymentMockMvc.perform(delete("/api/prepayments/{id}", prepayment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Prepayment> prepaymentList = prepaymentRepository.findAll();
        assertThat(prepaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Prepayment in Elasticsearch
        verify(mockPrepaymentSearchRepository, times(1)).deleteById(prepayment.getId());
    }

    @Test
    @Transactional
    public void searchPrepayment() throws Exception {
        // Initialize the database
        prepaymentRepository.saveAndFlush(prepayment);
        when(mockPrepaymentSearchRepository.search(queryStringQuery("id:" + prepayment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepayment), PageRequest.of(0, 1), 1));
        // Search the prepayment
        restPrepaymentMockMvc.perform(get("/api/_search/prepayments?query=id:" + prepayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepaymentAccount").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT)))
            .andExpect(jsonPath("$.[*].prepaymentAccountNumber").value(hasItem(DEFAULT_PREPAYMENT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].narration").value(hasItem(DEFAULT_NARRATION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].serviceOutletCode").value(hasItem(DEFAULT_SERVICE_OUTLET_CODE)))
            .andExpect(jsonPath("$.[*].expenseAccount").value(hasItem(DEFAULT_EXPENSE_ACCOUNT)))
            .andExpect(jsonPath("$.[*].expenseAccountNumber").value(hasItem(DEFAULT_EXPENSE_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].prepaymentTerm").value(hasItem(DEFAULT_PREPAYMENT_TERM)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prepayment.class);
        Prepayment prepayment1 = new Prepayment();
        prepayment1.setId(1L);
        Prepayment prepayment2 = new Prepayment();
        prepayment2.setId(prepayment1.getId());
        assertThat(prepayment1).isEqualTo(prepayment2);
        prepayment2.setId(2L);
        assertThat(prepayment1).isNotEqualTo(prepayment2);
        prepayment1.setId(null);
        assertThat(prepayment1).isNotEqualTo(prepayment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrepaymentDTO.class);
        PrepaymentDTO prepaymentDTO1 = new PrepaymentDTO();
        prepaymentDTO1.setId(1L);
        PrepaymentDTO prepaymentDTO2 = new PrepaymentDTO();
        assertThat(prepaymentDTO1).isNotEqualTo(prepaymentDTO2);
        prepaymentDTO2.setId(prepaymentDTO1.getId());
        assertThat(prepaymentDTO1).isEqualTo(prepaymentDTO2);
        prepaymentDTO2.setId(2L);
        assertThat(prepaymentDTO1).isNotEqualTo(prepaymentDTO2);
        prepaymentDTO1.setId(null);
        assertThat(prepaymentDTO1).isNotEqualTo(prepaymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(prepaymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(prepaymentMapper.fromId(null)).isNull();
    }
}
