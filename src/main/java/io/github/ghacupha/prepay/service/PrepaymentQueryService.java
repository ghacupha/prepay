package io.github.ghacupha.prepay.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.ghacupha.prepay.domain.Prepayment;
import io.github.ghacupha.prepay.domain.*; // for static metamodels
import io.github.ghacupha.prepay.repository.PrepaymentRepository;
import io.github.ghacupha.prepay.repository.search.PrepaymentSearchRepository;
import io.github.ghacupha.prepay.service.dto.PrepaymentCriteria;
import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;
import io.github.ghacupha.prepay.service.mapper.PrepaymentMapper;

/**
 * Service for executing complex queries for Prepayment entities in the database.
 * The main input is a {@link PrepaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentDTO} or a {@link Page} of {@link PrepaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentQueryService extends QueryService<Prepayment> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentQueryService.class);

    private final PrepaymentRepository prepaymentRepository;

    private final PrepaymentMapper prepaymentMapper;

    private final PrepaymentSearchRepository prepaymentSearchRepository;

    public PrepaymentQueryService(PrepaymentRepository prepaymentRepository, PrepaymentMapper prepaymentMapper, PrepaymentSearchRepository prepaymentSearchRepository) {
        this.prepaymentRepository = prepaymentRepository;
        this.prepaymentMapper = prepaymentMapper;
        this.prepaymentSearchRepository = prepaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentDTO> findByCriteria(PrepaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prepayment> specification = createSpecification(criteria);
        return prepaymentMapper.toDto(prepaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentDTO> findByCriteria(PrepaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prepayment> specification = createSpecification(criteria);
        return prepaymentRepository.findAll(specification, page)
            .map(prepaymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Prepayment> specification = createSpecification(criteria);
        return prepaymentRepository.count(specification);
    }

    /**
     * Function to convert PrepaymentCriteria to a {@link Specification}
     */
    private Specification<Prepayment> createSpecification(PrepaymentCriteria criteria) {
        Specification<Prepayment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Prepayment_.id));
            }
            if (criteria.getPrepaymentAccount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrepaymentAccount(), Prepayment_.prepaymentAccount));
            }
            if (criteria.getPrepaymentAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrepaymentAccountNumber(), Prepayment_.prepaymentAccountNumber));
            }
            if (criteria.getNarration() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNarration(), Prepayment_.narration));
            }
            if (criteria.getRemarks() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRemarks(), Prepayment_.remarks));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServiceOutletCode(), Prepayment_.serviceOutletCode));
            }
            if (criteria.getExpenseAccount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpenseAccount(), Prepayment_.expenseAccount));
            }
            if (criteria.getExpenseAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExpenseAccountNumber(), Prepayment_.expenseAccountNumber));
            }
            if (criteria.getTransactionId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransactionId(), Prepayment_.transactionId));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), Prepayment_.transactionDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Prepayment_.amount));
            }
            if (criteria.getPrepaymentTerm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrepaymentTerm(), Prepayment_.prepaymentTerm));
            }
        }
        return specification;
    }
}
