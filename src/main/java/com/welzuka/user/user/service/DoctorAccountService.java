package com.welzuka.user.user.service;

import com.welzuka.user.user.service.dto.DoctorAccountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing DoctorAccount.
 */
public interface DoctorAccountService {

    /**
     * Save a doctorAccount.
     *
     * @param doctorAccountDTO the entity to save
     * @return the persisted entity
     */
    DoctorAccountDTO save(DoctorAccountDTO doctorAccountDTO);

    /**
     * Get all the doctorAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorAccountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" doctorAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DoctorAccountDTO> findOne(Long id);

    /**
     * Delete the "id" doctorAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the doctorAccount corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorAccountDTO> search(String query, Pageable pageable);
}
