package com.welzuka.user.user.web.rest;

import com.welzuka.user.user.UserApp;

import com.welzuka.user.user.domain.DoctorAccount;
import com.welzuka.user.user.repository.DoctorAccountRepository;
import com.welzuka.user.user.repository.search.DoctorAccountSearchRepository;
import com.welzuka.user.user.service.DoctorAccountService;
import com.welzuka.user.user.service.dto.DoctorAccountDTO;
import com.welzuka.user.user.service.mapper.DoctorAccountMapper;
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
 * Test class for the DoctorAccountResource REST controller.
 *
 * @see DoctorAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
public class DoctorAccountResourceIntTest {

    private static final Long DEFAULT_ACCOUND_ID = 1L;
    private static final Long UPDATED_ACCOUND_ID = 2L;

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIENCE = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIALITY = "AAAAAAAAAA";
    private static final String UPDATED_SPECIALITY = "BBBBBBBBBB";

    private static final String DEFAULT_QUALIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_QUALIFICATION = "BBBBBBBBBB";

    @Autowired
    private DoctorAccountRepository doctorAccountRepository;

    @Autowired
    private DoctorAccountMapper doctorAccountMapper;

    @Autowired
    private DoctorAccountService doctorAccountService;

    /**
     * This repository is mocked in the com.welzuka.user.user.repository.search test package.
     *
     * @see com.welzuka.user.user.repository.search.DoctorAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorAccountSearchRepository mockDoctorAccountSearchRepository;

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

    private MockMvc restDoctorAccountMockMvc;

    private DoctorAccount doctorAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorAccountResource doctorAccountResource = new DoctorAccountResource(doctorAccountService);
        this.restDoctorAccountMockMvc = MockMvcBuilders.standaloneSetup(doctorAccountResource)
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
    public static DoctorAccount createEntity(EntityManager em) {
        DoctorAccount doctorAccount = new DoctorAccount()
            .accoundId(DEFAULT_ACCOUND_ID)
            .licenseNumber(DEFAULT_LICENSE_NUMBER)
            .experience(DEFAULT_EXPERIENCE)
            .speciality(DEFAULT_SPECIALITY)
            .qualification(DEFAULT_QUALIFICATION);
        return doctorAccount;
    }

    @Before
    public void initTest() {
        doctorAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorAccount() throws Exception {
        int databaseSizeBeforeCreate = doctorAccountRepository.findAll().size();

        // Create the DoctorAccount
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);
        restDoctorAccountMockMvc.perform(post("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the DoctorAccount in the database
        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorAccount testDoctorAccount = doctorAccountList.get(doctorAccountList.size() - 1);
        assertThat(testDoctorAccount.getAccoundId()).isEqualTo(DEFAULT_ACCOUND_ID);
        assertThat(testDoctorAccount.getLicenseNumber()).isEqualTo(DEFAULT_LICENSE_NUMBER);
        assertThat(testDoctorAccount.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testDoctorAccount.getSpeciality()).isEqualTo(DEFAULT_SPECIALITY);
        assertThat(testDoctorAccount.getQualification()).isEqualTo(DEFAULT_QUALIFICATION);

        // Validate the DoctorAccount in Elasticsearch
        verify(mockDoctorAccountSearchRepository, times(1)).save(testDoctorAccount);
    }

    @Test
    @Transactional
    public void createDoctorAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorAccountRepository.findAll().size();

        // Create the DoctorAccount with an existing ID
        doctorAccount.setId(1L);
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorAccountMockMvc.perform(post("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorAccount in the database
        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorAccount in Elasticsearch
        verify(mockDoctorAccountSearchRepository, times(0)).save(doctorAccount);
    }

    @Test
    @Transactional
    public void checkAccoundIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorAccountRepository.findAll().size();
        // set the field null
        doctorAccount.setAccoundId(null);

        // Create the DoctorAccount, which fails.
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);

        restDoctorAccountMockMvc.perform(post("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isBadRequest());

        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenseNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorAccountRepository.findAll().size();
        // set the field null
        doctorAccount.setLicenseNumber(null);

        // Create the DoctorAccount, which fails.
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);

        restDoctorAccountMockMvc.perform(post("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isBadRequest());

        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSpecialityIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorAccountRepository.findAll().size();
        // set the field null
        doctorAccount.setSpeciality(null);

        // Create the DoctorAccount, which fails.
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);

        restDoctorAccountMockMvc.perform(post("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isBadRequest());

        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctorAccounts() throws Exception {
        // Initialize the database
        doctorAccountRepository.saveAndFlush(doctorAccount);

        // Get all the doctorAccountList
        restDoctorAccountMockMvc.perform(get("/api/doctor-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accoundId").value(hasItem(DEFAULT_ACCOUND_ID.intValue())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY.toString())))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getDoctorAccount() throws Exception {
        // Initialize the database
        doctorAccountRepository.saveAndFlush(doctorAccount);

        // Get the doctorAccount
        restDoctorAccountMockMvc.perform(get("/api/doctor-accounts/{id}", doctorAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorAccount.getId().intValue()))
            .andExpect(jsonPath("$.accoundId").value(DEFAULT_ACCOUND_ID.intValue()))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.toString()))
            .andExpect(jsonPath("$.speciality").value(DEFAULT_SPECIALITY.toString()))
            .andExpect(jsonPath("$.qualification").value(DEFAULT_QUALIFICATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorAccount() throws Exception {
        // Get the doctorAccount
        restDoctorAccountMockMvc.perform(get("/api/doctor-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorAccount() throws Exception {
        // Initialize the database
        doctorAccountRepository.saveAndFlush(doctorAccount);

        int databaseSizeBeforeUpdate = doctorAccountRepository.findAll().size();

        // Update the doctorAccount
        DoctorAccount updatedDoctorAccount = doctorAccountRepository.findById(doctorAccount.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorAccount are not directly saved in db
        em.detach(updatedDoctorAccount);
        updatedDoctorAccount
            .accoundId(UPDATED_ACCOUND_ID)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .experience(UPDATED_EXPERIENCE)
            .speciality(UPDATED_SPECIALITY)
            .qualification(UPDATED_QUALIFICATION);
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(updatedDoctorAccount);

        restDoctorAccountMockMvc.perform(put("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isOk());

        // Validate the DoctorAccount in the database
        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeUpdate);
        DoctorAccount testDoctorAccount = doctorAccountList.get(doctorAccountList.size() - 1);
        assertThat(testDoctorAccount.getAccoundId()).isEqualTo(UPDATED_ACCOUND_ID);
        assertThat(testDoctorAccount.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testDoctorAccount.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testDoctorAccount.getSpeciality()).isEqualTo(UPDATED_SPECIALITY);
        assertThat(testDoctorAccount.getQualification()).isEqualTo(UPDATED_QUALIFICATION);

        // Validate the DoctorAccount in Elasticsearch
        verify(mockDoctorAccountSearchRepository, times(1)).save(testDoctorAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorAccount() throws Exception {
        int databaseSizeBeforeUpdate = doctorAccountRepository.findAll().size();

        // Create the DoctorAccount
        DoctorAccountDTO doctorAccountDTO = doctorAccountMapper.toDto(doctorAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorAccountMockMvc.perform(put("/api/doctor-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorAccount in the database
        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorAccount in Elasticsearch
        verify(mockDoctorAccountSearchRepository, times(0)).save(doctorAccount);
    }

    @Test
    @Transactional
    public void deleteDoctorAccount() throws Exception {
        // Initialize the database
        doctorAccountRepository.saveAndFlush(doctorAccount);

        int databaseSizeBeforeDelete = doctorAccountRepository.findAll().size();

        // Delete the doctorAccount
        restDoctorAccountMockMvc.perform(delete("/api/doctor-accounts/{id}", doctorAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DoctorAccount> doctorAccountList = doctorAccountRepository.findAll();
        assertThat(doctorAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorAccount in Elasticsearch
        verify(mockDoctorAccountSearchRepository, times(1)).deleteById(doctorAccount.getId());
    }

    @Test
    @Transactional
    public void searchDoctorAccount() throws Exception {
        // Initialize the database
        doctorAccountRepository.saveAndFlush(doctorAccount);
        when(mockDoctorAccountSearchRepository.search(queryStringQuery("id:" + doctorAccount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorAccount), PageRequest.of(0, 1), 1));
        // Search the doctorAccount
        restDoctorAccountMockMvc.perform(get("/api/_search/doctor-accounts?query=id:" + doctorAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accoundId").value(hasItem(DEFAULT_ACCOUND_ID.intValue())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].speciality").value(hasItem(DEFAULT_SPECIALITY)))
            .andExpect(jsonPath("$.[*].qualification").value(hasItem(DEFAULT_QUALIFICATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorAccount.class);
        DoctorAccount doctorAccount1 = new DoctorAccount();
        doctorAccount1.setId(1L);
        DoctorAccount doctorAccount2 = new DoctorAccount();
        doctorAccount2.setId(doctorAccount1.getId());
        assertThat(doctorAccount1).isEqualTo(doctorAccount2);
        doctorAccount2.setId(2L);
        assertThat(doctorAccount1).isNotEqualTo(doctorAccount2);
        doctorAccount1.setId(null);
        assertThat(doctorAccount1).isNotEqualTo(doctorAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorAccountDTO.class);
        DoctorAccountDTO doctorAccountDTO1 = new DoctorAccountDTO();
        doctorAccountDTO1.setId(1L);
        DoctorAccountDTO doctorAccountDTO2 = new DoctorAccountDTO();
        assertThat(doctorAccountDTO1).isNotEqualTo(doctorAccountDTO2);
        doctorAccountDTO2.setId(doctorAccountDTO1.getId());
        assertThat(doctorAccountDTO1).isEqualTo(doctorAccountDTO2);
        doctorAccountDTO2.setId(2L);
        assertThat(doctorAccountDTO1).isNotEqualTo(doctorAccountDTO2);
        doctorAccountDTO1.setId(null);
        assertThat(doctorAccountDTO1).isNotEqualTo(doctorAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(doctorAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(doctorAccountMapper.fromId(null)).isNull();
    }
}
