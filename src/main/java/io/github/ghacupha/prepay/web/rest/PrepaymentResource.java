package io.github.ghacupha.prepay.web.rest;
import io.github.ghacupha.prepay.service.IPrepaymentService;
import io.github.ghacupha.prepay.web.rest.errors.BadRequestAlertException;
import io.github.ghacupha.prepay.web.rest.util.HeaderUtil;
import io.github.ghacupha.prepay.web.rest.util.PaginationUtil;
import io.github.ghacupha.prepay.service.dto.PrepaymentDTO;
import io.github.ghacupha.prepay.service.dto.PrepaymentCriteria;
import io.github.ghacupha.prepay.service.PrepaymentQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Prepayment.
 */
@RestController
@RequestMapping("/api")
public class PrepaymentResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentResource.class);

    private static final String ENTITY_NAME = "prepayment";

    private final IPrepaymentService IPrepaymentService;

    private final PrepaymentQueryService prepaymentQueryService;

    public PrepaymentResource(IPrepaymentService IPrepaymentService, PrepaymentQueryService prepaymentQueryService) {
        this.IPrepaymentService = IPrepaymentService;
        this.prepaymentQueryService = prepaymentQueryService;
    }

    /**
     * POST  /prepayments : Create a new prepayment.
     *
     * @param prepaymentDTO the prepaymentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prepaymentDTO, or with status 400 (Bad Request) if the prepayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prepayments")
    public ResponseEntity<PrepaymentDTO> createPrepayment(@Valid @RequestBody PrepaymentDTO prepaymentDTO) throws URISyntaxException {
        log.debug("REST request to save Prepayment : {}", prepaymentDTO);
        if (prepaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentDTO result = IPrepaymentService.save(prepaymentDTO);
        return ResponseEntity.created(new URI("/api/prepayments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prepayments : Updates an existing prepayment.
     *
     * @param prepaymentDTO the prepaymentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prepaymentDTO,
     * or with status 400 (Bad Request) if the prepaymentDTO is not valid,
     * or with status 500 (Internal Server Error) if the prepaymentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prepayments")
    public ResponseEntity<PrepaymentDTO> updatePrepayment(@Valid @RequestBody PrepaymentDTO prepaymentDTO) throws URISyntaxException {
        log.debug("REST request to update Prepayment : {}", prepaymentDTO);
        if (prepaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrepaymentDTO result = IPrepaymentService.save(prepaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prepaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prepayments : get all the prepayments.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of prepayments in body
     */
    @GetMapping("/prepayments")
    public ResponseEntity<List<PrepaymentDTO>> getAllPrepayments(PrepaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Prepayments by criteria: {}", criteria);
        Page<PrepaymentDTO> page = prepaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/prepayments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /prepayments/count : count all the prepayments.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/prepayments/count")
    public ResponseEntity<Long> countPrepayments(PrepaymentCriteria criteria) {
        log.debug("REST request to count Prepayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /prepayments/:id : get the "id" prepayment.
     *
     * @param id the id of the prepaymentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prepaymentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/prepayments/{id}")
    public ResponseEntity<PrepaymentDTO> getPrepayment(@PathVariable Long id) {
        log.debug("REST request to get Prepayment : {}", id);
        Optional<PrepaymentDTO> prepaymentDTO = IPrepaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentDTO);
    }

    /**
     * DELETE  /prepayments/:id : delete the "id" prepayment.
     *
     * @param id the id of the prepaymentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prepayments/{id}")
    public ResponseEntity<Void> deletePrepayment(@PathVariable Long id) {
        log.debug("REST request to delete Prepayment : {}", id);
        IPrepaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/prepayments?query=:query : search for the prepayment corresponding
     * to the query.
     *
     * @param query the query of the prepayment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/prepayments")
    public ResponseEntity<List<PrepaymentDTO>> searchPrepayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Prepayments for query {}", query);
        Page<PrepaymentDTO> page = IPrepaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/prepayments");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
