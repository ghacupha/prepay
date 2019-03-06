package io.github.ghacupha.prepay.service.impl;

import io.github.ghacupha.prepay.service.PrepaymentService;
import io.github.ghacupha.prepay.domain.Prepayment;
import io.github.ghacupha.prepay.repository.PrepaymentRepository;
import io.github.ghacupha.prepay.repository.search.PrepaymentSearchRepository;
import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;
import io.github.ghacupha.prepay.service.mapper.PrepaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Prepayment.
 */
@Service
@Transactional
public class PrepaymentServiceImpl implements PrepaymentService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentServiceImpl.class);

    private final PrepaymentRepository prepaymentRepository;

    private final PrepaymentMapper prepaymentMapper;

    private final PrepaymentSearchRepository prepaymentSearchRepository;

    public PrepaymentServiceImpl(PrepaymentRepository prepaymentRepository, PrepaymentMapper prepaymentMapper, PrepaymentSearchRepository prepaymentSearchRepository) {
        this.prepaymentRepository = prepaymentRepository;
        this.prepaymentMapper = prepaymentMapper;
        this.prepaymentSearchRepository = prepaymentSearchRepository;
    }

    /**
     * Save a prepayment.
     *
     * @param prepaymentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrepaymentDTO save(PrepaymentDTO prepaymentDTO) {
        log.debug("Request to save Prepayment : {}", prepaymentDTO);
        Prepayment prepayment = prepaymentMapper.toEntity(prepaymentDTO);
        prepayment = prepaymentRepository.save(prepayment);
        PrepaymentDTO result = prepaymentMapper.toDto(prepayment);
        prepaymentSearchRepository.save(prepayment);
        return result;
    }

    /**
     * Get all the prepayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prepayments");
        return prepaymentRepository.findAll(pageable)
            .map(prepaymentMapper::toDto);
    }


    /**
     * Get one prepayment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentDTO> findOne(Long id) {
        log.debug("Request to get Prepayment : {}", id);
        return prepaymentRepository.findById(id)
            .map(prepaymentMapper::toDto);
    }

    /**
     * Delete the prepayment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prepayment : {}", id);
        prepaymentRepository.deleteById(id);
        prepaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the prepayment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Prepayments for query {}", query);
        return prepaymentSearchRepository.search(queryStringQuery(query), pageable)
            .map(prepaymentMapper::toDto);
    }
}
