package io.github.ghacupha.prepay.service;

import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Prepayment.
 */
public interface PrepaymentService {

    /**
     * Save a prepayment.
     *
     * @param prepaymentDTO the entity to save
     * @return the persisted entity
     */
    PrepaymentDTO save(PrepaymentDTO prepaymentDTO);

    /**
     * Get all the prepayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrepaymentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" prepayment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PrepaymentDTO> findOne(Long id);

    /**
     * Delete the "id" prepayment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the prepayment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PrepaymentDTO> search(String query, Pageable pageable);
}
