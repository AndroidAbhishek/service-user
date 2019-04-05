package com.welzuka.user.user.service;

import com.welzuka.user.user.domain.UserAccount;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing UserAccount.
 */
public interface UserAccountService {

    /**
     * Save a userAccount.
     *
     * @param userAccount the entity to save
     * @return the persisted entity
     */
    UserAccount save(UserAccount userAccount);

    /**
     * Get all the userAccounts.
     *
     * @return the list of entities
     */
    List<UserAccount> findAll();


    /**
     * Get the "id" userAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserAccount> findOne(Long id);

    /**
     * Delete the "id" userAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the userAccount corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<UserAccount> search(String query);
}
