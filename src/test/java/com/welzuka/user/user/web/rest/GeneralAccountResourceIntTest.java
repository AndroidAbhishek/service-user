package com.welzuka.user.user.web.rest;

import com.welzuka.user.user.UserApp;

import com.welzuka.user.user.domain.GeneralAccount;
import com.welzuka.user.user.repository.GeneralAccountRepository;
import com.welzuka.user.user.repository.search.GeneralAccountSearchRepository;
import com.welzuka.user.user.service.GeneralAccountService;
import com.welzuka.user.user.service.dto.GeneralAccountDTO;
import com.welzuka.user.user.service.mapper.GeneralAccountMapper;
import com.welzuka.user.user.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.welzuka.user.user.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GeneralAccountResource REST controller.
 *
 * @see GeneralAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
public class GeneralAccountResourceIntTest {

    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final String DEFAULT_PERSONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_INFO = "BBBBBBBBBB";

    @Autowired
    private GeneralAccountRepository generalAccountRepository;

    @Autowired
    private GeneralAccountMapper generalAccountMapper;

    @Autowired
    private GeneralAccountService generalAccountService;

    /**
     * This repository is mocked in the com.welzuka.user.user.repository.search test package.
     *
     * @see com.welzuka.user.user.repository.search.GeneralAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private GeneralAccountSearchRepository mockGeneralAccountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGeneralAccountMockMvc;

    private GeneralAccount generalAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeneralAccountResource generalAccountResource = new GeneralAccountResource(generalAccountService);
        this.restGeneralAccountMockMvc = MockMvcBuilders.standaloneSetup(generalAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GeneralAccount createEntity(EntityManager em) {
        GeneralAccount generalAccount = new GeneralAccount()
            .accountId(DEFAULT_ACCOUNT_ID)
            .personalInfo(DEFAULT_PERSONAL_INFO);
        return generalAccount;
    }

    @Before
    public void initTest() {
        generalAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createGeneralAccount() throws Exception {
        int databaseSizeBeforeCreate = generalAccountRepository.findAll().size();

        // Create the GeneralAccount
        GeneralAccountDTO generalAccountDTO = generalAccountMapper.toDto(generalAccount);
        restGeneralAccountMockMvc.perform(post("/api/general-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the GeneralAccount in the database
        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeCreate + 1);
        GeneralAccount testGeneralAccount = generalAccountList.get(generalAccountList.size() - 1);
        assertThat(testGeneralAccount.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testGeneralAccount.getPersonalInfo()).isEqualTo(DEFAULT_PERSONAL_INFO);

        // Validate the GeneralAccount in Elasticsearch
        verify(mockGeneralAccountSearchRepository, times(1)).save(testGeneralAccount);
    }

    @Test
    @Transactional
    public void createGeneralAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = generalAccountRepository.findAll().size();

        // Create the GeneralAccount with an existing ID
        generalAccount.setId(1L);
        GeneralAccountDTO generalAccountDTO = generalAccountMapper.toDto(generalAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeneralAccountMockMvc.perform(post("/api/general-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeneralAccount in the database
        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the GeneralAccount in Elasticsearch
        verify(mockGeneralAccountSearchRepository, times(0)).save(generalAccount);
    }

    @Test
    @Transactional
    public void checkAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = generalAccountRepository.findAll().size();
        // set the field null
        generalAccount.setAccountId(null);

        // Create the GeneralAccount, which fails.
        GeneralAccountDTO generalAccountDTO = generalAccountMapper.toDto(generalAccount);

        restGeneralAccountMockMvc.perform(post("/api/general-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalAccountDTO)))
            .andExpect(status().isBadRequest());

        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeneralAccounts() throws Exception {
        // Initialize the database
        generalAccountRepository.saveAndFlush(generalAccount);

        // Get all the generalAccountList
        restGeneralAccountMockMvc.perform(get("/api/general-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generalAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].personalInfo").value(hasItem(DEFAULT_PERSONAL_INFO.toString())));
    }
    
    @Test
    @Transactional
    public void getGeneralAccount() throws Exception {
        // Initialize the database
        generalAccountRepository.saveAndFlush(generalAccount);

        // Get the generalAccount
        restGeneralAccountMockMvc.perform(get("/api/general-accounts/{id}", generalAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(generalAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.personalInfo").value(DEFAULT_PERSONAL_INFO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGeneralAccount() throws Exception {
        // Get the generalAccount
        restGeneralAccountMockMvc.perform(get("/api/general-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeneralAccount() throws Exception {
        // Initialize the database
        generalAccountRepository.saveAndFlush(generalAccount);

        int databaseSizeBeforeUpdate = generalAccountRepository.findAll().size();

        // Update the generalAccount
        GeneralAccount updatedGeneralAccount = generalAccountRepository.findById(generalAccount.getId()).get();
        // Disconnect from session so that the updates on updatedGeneralAccount are not directly saved in db
        em.detach(updatedGeneralAccount);
        updatedGeneralAccount
            .accountId(UPDATED_ACCOUNT_ID)
            .personalInfo(UPDATED_PERSONAL_INFO);
        GeneralAccountDTO generalAccountDTO = generalAccountMapper.toDto(updatedGeneralAccount);

        restGeneralAccountMockMvc.perform(put("/api/general-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalAccountDTO)))
            .andExpect(status().isOk());

        // Validate the GeneralAccount in the database
        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeUpdate);
        GeneralAccount testGeneralAccount = generalAccountList.get(generalAccountList.size() - 1);
        assertThat(testGeneralAccount.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testGeneralAccount.getPersonalInfo()).isEqualTo(UPDATED_PERSONAL_INFO);

        // Validate the GeneralAccount in Elasticsearch
        verify(mockGeneralAccountSearchRepository, times(1)).save(testGeneralAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingGeneralAccount() throws Exception {
        int databaseSizeBeforeUpdate = generalAccountRepository.findAll().size();

        // Create the GeneralAccount
        GeneralAccountDTO generalAccountDTO = generalAccountMapper.toDto(generalAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeneralAccountMockMvc.perform(put("/api/general-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(generalAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeneralAccount in the database
        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GeneralAccount in Elasticsearch
        verify(mockGeneralAccountSearchRepository, times(0)).save(generalAccount);
    }

    @Test
    @Transactional
    public void deleteGeneralAccount() throws Exception {
        // Initialize the database
        generalAccountRepository.saveAndFlush(generalAccount);

        int databaseSizeBeforeDelete = generalAccountRepository.findAll().size();

        // Delete the generalAccount
        restGeneralAccountMockMvc.perform(delete("/api/general-accounts/{id}", generalAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeneralAccount> generalAccountList = generalAccountRepository.findAll();
        assertThat(generalAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GeneralAccount in Elasticsearch
        verify(mockGeneralAccountSearchRepository, times(1)).deleteById(generalAccount.getId());
    }

    @Test
    @Transactional
    public void searchGeneralAccount() throws Exception {
        // Initialize the database
        generalAccountRepository.saveAndFlush(generalAccount);
        when(mockGeneralAccountSearchRepository.search(queryStringQuery("id:" + generalAccount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(generalAccount), PageRequest.of(0, 1), 1));
        // Search the generalAccount
        restGeneralAccountMockMvc.perform(get("/api/_search/general-accounts?query=id:" + generalAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generalAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].personalInfo").value(hasItem(DEFAULT_PERSONAL_INFO)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneralAccount.class);
        GeneralAccount generalAccount1 = new GeneralAccount();
        generalAccount1.setId(1L);
        GeneralAccount generalAccount2 = new GeneralAccount();
        generalAccount2.setId(generalAccount1.getId());
        assertThat(generalAccount1).isEqualTo(generalAccount2);
        generalAccount2.setId(2L);
        assertThat(generalAccount1).isNotEqualTo(generalAccount2);
        generalAccount1.setId(null);
        assertThat(generalAccount1).isNotEqualTo(generalAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeneralAccountDTO.class);
        GeneralAccountDTO generalAccountDTO1 = new GeneralAccountDTO();
        generalAccountDTO1.setId(1L);
        GeneralAccountDTO generalAccountDTO2 = new GeneralAccountDTO();
        assertThat(generalAccountDTO1).isNotEqualTo(generalAccountDTO2);
        generalAccountDTO2.setId(generalAccountDTO1.getId());
        assertThat(generalAccountDTO1).isEqualTo(generalAccountDTO2);
        generalAccountDTO2.setId(2L);
        assertThat(generalAccountDTO1).isNotEqualTo(generalAccountDTO2);
        generalAccountDTO1.setId(null);
        assertThat(generalAccountDTO1).isNotEqualTo(generalAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(generalAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(generalAccountMapper.fromId(null)).isNull();
    }
}
