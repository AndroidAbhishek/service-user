package com.welzuka.user.user.service.impl;

import com.welzuka.user.user.service.GeneralAccountService;
import com.welzuka.user.user.domain.GeneralAccount;
import com.welzuka.user.user.repository.GeneralAccountRepository;
import com.welzuka.user.user.repository.search.GeneralAccountSearchRepository;
import com.welzuka.user.user.service.dto.GeneralAccountDTO;
import com.welzuka.user.user.service.mapper.GeneralAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing GeneralAccount.
 */
@Service
@Transactional
public class GeneralAccountServiceImpl implements GeneralAccountService {

    private final Logger log = LoggerFactory.getLogger(GeneralAccountServiceImpl.class);

    private final GeneralAccountRepository generalAccountRepository;

    private final GeneralAccountMapper generalAccountMapper;

    private final GeneralAccountSearchRepository generalAccountSearchRepository;

    public GeneralAccountServiceImpl(GeneralAccountRepository generalAccountRepository, GeneralAccountMapper generalAccountMapper, GeneralAccountSearchRepository generalAccountSearchRepository) {
        this.generalAccountRepository = generalAccountRepository;
        this.generalAccountMapper = generalAccountMapper;
        this.generalAccountSearchRepository = generalAccountSearchRepository;
    }

    /**
     * Save a generalAccount.
     *
     * @param generalAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GeneralAccountDTO save(GeneralAccountDTO generalAccountDTO) {
        log.debug("Request to save GeneralAccount : {}", generalAccountDTO);
        GeneralAccount generalAccount = generalAccountMapper.toEntity(generalAccountDTO);
        generalAccount = generalAccountRepository.save(generalAccount);
        GeneralAccountDTO result = generalAccountMapper.toDto(generalAccount);
        generalAccountSearchRepository.save(generalAccount);
        return result;
    }

    /**
     * Get all the generalAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeneralAccounts");
        return generalAccountRepository.findAll(pageable)
            .map(generalAccountMapper::toDto);
    }


    /**
     * Get one generalAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GeneralAccountDTO> findOne(Long id) {
        log.debug("Request to get GeneralAccount : {}", id);
        return generalAccountRepository.findById(id)
            .map(generalAccountMapper::toDto);
    }

    /**
     * Delete the generalAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GeneralAccount : {}", id);
        generalAccountRepository.deleteById(id);
        generalAccountSearchRepository.deleteById(id);
    }

    /**
     * Search for the generalAccount corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GeneralAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GeneralAccounts for query {}", query);
        return generalAccountSearchRepository.search(queryStringQuery(query), pageable)
            .map(generalAccountMapper::toDto);
    }
}
