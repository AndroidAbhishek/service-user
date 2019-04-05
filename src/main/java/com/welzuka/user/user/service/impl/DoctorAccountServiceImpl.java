package com.welzuka.user.user.service.impl;

import com.welzuka.user.user.service.DoctorAccountService;
import com.welzuka.user.user.domain.DoctorAccount;
import com.welzuka.user.user.repository.DoctorAccountRepository;
import com.welzuka.user.user.repository.search.DoctorAccountSearchRepository;
import com.welzuka.user.user.service.dto.DoctorAccountDTO;
import com.welzuka.user.user.service.mapper.DoctorAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing DoctorAccount.
 */
@Service
@Transactional
public class DoctorAccountServiceImpl implements DoctorAccountService {

    private final Logger log = LoggerFactory.getLogger(DoctorAccountServiceImpl.class);

    private final DoctorAccountRepository doctorAccountRepository;

    private final DoctorAccountMapper doctorAccountMapper;

    private final DoctorAccountSearchRepository doctorAccountSearchRepository;

    public DoctorAccountServiceImpl(DoctorAccountRepository doctorAccountRepository, DoctorAccountMapper doctorAccountMapper, DoctorAccountSearchRepository doctorAccountSearchRepository) {
        this.doctorAccountRepository = doctorAccountRepository;
        this.doctorAccountMapper = doctorAccountMapper;
        this.doctorAccountSearchRepository = doctorAccountSearchRepository;
    }

    /**
     * Save a doctorAccount.
     *
     * @param doctorAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DoctorAccountDTO save(DoctorAccountDTO doctorAccountDTO) {
        log.debug("Request to save DoctorAccount : {}", doctorAccountDTO);
        DoctorAccount doctorAccount = doctorAccountMapper.toEntity(doctorAccountDTO);
        doctorAccount = doctorAccountRepository.save(doctorAccount);
        DoctorAccountDTO result = doctorAccountMapper.toDto(doctorAccount);
        doctorAccountSearchRepository.save(doctorAccount);
        return result;
    }

    /**
     * Get all the doctorAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorAccounts");
        return doctorAccountRepository.findAll(pageable)
            .map(doctorAccountMapper::toDto);
    }


    /**
     * Get one doctorAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorAccountDTO> findOne(Long id) {
        log.debug("Request to get DoctorAccount : {}", id);
        return doctorAccountRepository.findById(id)
            .map(doctorAccountMapper::toDto);
    }

    /**
     * Delete the doctorAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorAccount : {}", id);
        doctorAccountRepository.deleteById(id);
        doctorAccountSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorAccount corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorAccounts for query {}", query);
        return doctorAccountSearchRepository.search(queryStringQuery(query), pageable)
            .map(doctorAccountMapper::toDto);
    }
}
