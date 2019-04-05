package com.welzuka.user.user.web.rest;
import com.welzuka.user.user.service.DoctorAccountService;
import com.welzuka.user.user.web.rest.errors.BadRequestAlertException;
import com.welzuka.user.user.web.rest.util.HeaderUtil;
import com.welzuka.user.user.web.rest.util.PaginationUtil;
import com.welzuka.user.user.service.dto.DoctorAccountDTO;
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
 * REST controller for managing DoctorAccount.
 */
@RestController
@RequestMapping("/api")
public class DoctorAccountResource {

    private final Logger log = LoggerFactory.getLogger(DoctorAccountResource.class);

    private static final String ENTITY_NAME = "userDoctorAccount";

    private final DoctorAccountService doctorAccountService;

    public DoctorAccountResource(DoctorAccountService doctorAccountService) {
        this.doctorAccountService = doctorAccountService;
    }

    /**
     * POST  /doctor-accounts : Create a new doctorAccount.
     *
     * @param doctorAccountDTO the doctorAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctorAccountDTO, or with status 400 (Bad Request) if the doctorAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctor-accounts")
    public ResponseEntity<DoctorAccountDTO> createDoctorAccount(@Valid @RequestBody DoctorAccountDTO doctorAccountDTO) throws URISyntaxException {
        log.debug("REST request to save DoctorAccount : {}", doctorAccountDTO);
        if (doctorAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctorAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorAccountDTO result = doctorAccountService.save(doctorAccountDTO);
        return ResponseEntity.created(new URI("/api/doctor-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctor-accounts : Updates an existing doctorAccount.
     *
     * @param doctorAccountDTO the doctorAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctorAccountDTO,
     * or with status 400 (Bad Request) if the doctorAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the doctorAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctor-accounts")
    public ResponseEntity<DoctorAccountDTO> updateDoctorAccount(@Valid @RequestBody DoctorAccountDTO doctorAccountDTO) throws URISyntaxException {
        log.debug("REST request to update DoctorAccount : {}", doctorAccountDTO);
        if (doctorAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorAccountDTO result = doctorAccountService.save(doctorAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctor-accounts : get all the doctorAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doctorAccounts in body
     */
    @GetMapping("/doctor-accounts")
    public ResponseEntity<List<DoctorAccountDTO>> getAllDoctorAccounts(Pageable pageable) {
        log.debug("REST request to get a page of DoctorAccounts");
        Page<DoctorAccountDTO> page = doctorAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctor-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /doctor-accounts/:id : get the "id" doctorAccount.
     *
     * @param id the id of the doctorAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctorAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/doctor-accounts/{id}")
    public ResponseEntity<DoctorAccountDTO> getDoctorAccount(@PathVariable Long id) {
        log.debug("REST request to get DoctorAccount : {}", id);
        Optional<DoctorAccountDTO> doctorAccountDTO = doctorAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorAccountDTO);
    }

    /**
     * DELETE  /doctor-accounts/:id : delete the "id" doctorAccount.
     *
     * @param id the id of the doctorAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctor-accounts/{id}")
    public ResponseEntity<Void> deleteDoctorAccount(@PathVariable Long id) {
        log.debug("REST request to delete DoctorAccount : {}", id);
        doctorAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctor-accounts?query=:query : search for the doctorAccount corresponding
     * to the query.
     *
     * @param query the query of the doctorAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctor-accounts")
    public ResponseEntity<List<DoctorAccountDTO>> searchDoctorAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorAccounts for query {}", query);
        Page<DoctorAccountDTO> page = doctorAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/doctor-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
