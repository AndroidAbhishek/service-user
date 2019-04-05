package com.welzuka.user.user.web.rest;
import com.welzuka.user.user.service.GeneralAccountService;
import com.welzuka.user.user.web.rest.errors.BadRequestAlertException;
import com.welzuka.user.user.web.rest.util.HeaderUtil;
import com.welzuka.user.user.web.rest.util.PaginationUtil;
import com.welzuka.user.user.service.dto.GeneralAccountDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing GeneralAccount.
 */
@RestController
@RequestMapping("/api")
public class GeneralAccountResource {

    private final Logger log = LoggerFactory.getLogger(GeneralAccountResource.class);

    private static final String ENTITY_NAME = "userGeneralAccount";

    private final GeneralAccountService generalAccountService;

    public GeneralAccountResource(GeneralAccountService generalAccountService) {
        this.generalAccountService = generalAccountService;
    }

    /**
     * POST  /general-accounts : Create a new generalAccount.
     *
     * @param generalAccountDTO the generalAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new generalAccountDTO, or with status 400 (Bad Request) if the generalAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/general-accounts")
    public ResponseEntity<GeneralAccountDTO> createGeneralAccount(@Valid @RequestBody GeneralAccountDTO generalAccountDTO) throws URISyntaxException {
        log.debug("REST request to save GeneralAccount : {}", generalAccountDTO);
        if (generalAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new generalAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeneralAccountDTO result = generalAccountService.save(generalAccountDTO);
        return ResponseEntity.created(new URI("/api/general-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /general-accounts : Updates an existing generalAccount.
     *
     * @param generalAccountDTO the generalAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated generalAccountDTO,
     * or with status 400 (Bad Request) if the generalAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the generalAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/general-accounts")
    public ResponseEntity<GeneralAccountDTO> updateGeneralAccount(@Valid @RequestBody GeneralAccountDTO generalAccountDTO) throws URISyntaxException {
        log.debug("REST request to update GeneralAccount : {}", generalAccountDTO);
        if (generalAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeneralAccountDTO result = generalAccountService.save(generalAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, generalAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /general-accounts : get all the generalAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of generalAccounts in body
     */
    @GetMapping("/general-accounts")
    public ResponseEntity<List<GeneralAccountDTO>> getAllGeneralAccounts(Pageable pageable) {
        log.debug("REST request to get a page of GeneralAccounts");
        Page<GeneralAccountDTO> page = generalAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/general-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /general-accounts/:id : get the "id" generalAccount.
     *
     * @param id the id of the generalAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the generalAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/general-accounts/{id}")
    public ResponseEntity<GeneralAccountDTO> getGeneralAccount(@PathVariable Long id) {
        log.debug("REST request to get GeneralAccount : {}", id);
        Optional<GeneralAccountDTO> generalAccountDTO = generalAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(generalAccountDTO);
    }

    /**
     * DELETE  /general-accounts/:id : delete the "id" generalAccount.
     *
     * @param id the id of the generalAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/general-accounts/{id}")
    public ResponseEntity<Void> deleteGeneralAccount(@PathVariable Long id) {
        log.debug("REST request to delete GeneralAccount : {}", id);
        generalAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/general-accounts?query=:query : search for the generalAccount corresponding
     * to the query.
     *
     * @param query the query of the generalAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/general-accounts")
    public ResponseEntity<List<GeneralAccountDTO>> searchGeneralAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GeneralAccounts for query {}", query);
        Page<GeneralAccountDTO> page = generalAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/general-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
