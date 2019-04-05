package com.welzuka.user.user.service;

import com.welzuka.user.user.service.dto.GeneralAccountDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing GeneralAccount.
 */
public interface GeneralAccountService {

    /**
     * Save a generalAccount.
     *
     * @param generalAccountDTO the entity to save
     * @return the persisted entity
     */
    GeneralAccountDTO save(GeneralAccountDTO generalAccountDTO);

    /**
     * Get all the generalAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeneralAccountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" generalAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<GeneralAccountDTO> findOne(Long id);

    /**
     * Delete the "id" generalAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the generalAccount corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<GeneralAccountDTO> search(String query, Pageable pageable);
}
