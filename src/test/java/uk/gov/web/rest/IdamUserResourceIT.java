package uk.gov.web.rest;

import uk.gov.UsermgmtdemoApp;
import uk.gov.config.TestSecurityConfiguration;
import uk.gov.domain.IdamUser;
import uk.gov.repository.IdamUserRepository;
import uk.gov.repository.search.IdamUserSearchRepository;
import uk.gov.service.IdamUserService;
import uk.gov.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static uk.gov.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link IdamUserResource} REST controller.
 */
@SpringBootTest(classes = {UsermgmtdemoApp.class, TestSecurityConfiguration.class})
public class IdamUserResourceIT {

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private IdamUserRepository idamUserRepository;

    @Mock
    private IdamUserRepository idamUserRepositoryMock;

    @Mock
    private IdamUserService idamUserServiceMock;

    @Autowired
    private IdamUserService idamUserService;

    /**
     * This repository is mocked in the uk.gov.repository.search test package.
     *
     * @see uk.gov.repository.search.IdamUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private IdamUserSearchRepository mockIdamUserSearchRepository;

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

    private MockMvc restIdamUserMockMvc;

    private IdamUser idamUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IdamUserResource idamUserResource = new IdamUserResource(idamUserService);
        this.restIdamUserMockMvc = MockMvcBuilders.standaloneSetup(idamUserResource)
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
    public static IdamUser createEntity(EntityManager em) {
        IdamUser idamUser = new IdamUser()
            .uid(DEFAULT_UID)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .status(DEFAULT_STATUS);
        return idamUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdamUser createUpdatedEntity(EntityManager em) {
        IdamUser idamUser = new IdamUser()
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .status(UPDATED_STATUS);
        return idamUser;
    }

    @BeforeEach
    public void initTest() {
        idamUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createIdamUser() throws Exception {
        int databaseSizeBeforeCreate = idamUserRepository.findAll().size();

        // Create the IdamUser
        restIdamUserMockMvc.perform(post("/api/idam-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamUser)))
            .andExpect(status().isCreated());

        // Validate the IdamUser in the database
        List<IdamUser> idamUserList = idamUserRepository.findAll();
        assertThat(idamUserList).hasSize(databaseSizeBeforeCreate + 1);
        IdamUser testIdamUser = idamUserList.get(idamUserList.size() - 1);
        assertThat(testIdamUser.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testIdamUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testIdamUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testIdamUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testIdamUser.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the IdamUser in Elasticsearch
        verify(mockIdamUserSearchRepository, times(1)).save(testIdamUser);
    }

    @Test
    @Transactional
    public void createIdamUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = idamUserRepository.findAll().size();

        // Create the IdamUser with an existing ID
        idamUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdamUserMockMvc.perform(post("/api/idam-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamUser)))
            .andExpect(status().isBadRequest());

        // Validate the IdamUser in the database
        List<IdamUser> idamUserList = idamUserRepository.findAll();
        assertThat(idamUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the IdamUser in Elasticsearch
        verify(mockIdamUserSearchRepository, times(0)).save(idamUser);
    }


    @Test
    @Transactional
    public void getAllIdamUsers() throws Exception {
        // Initialize the database
        idamUserRepository.saveAndFlush(idamUser);

        // Get all the idamUserList
        restIdamUserMockMvc.perform(get("/api/idam-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllIdamUsersWithEagerRelationshipsIsEnabled() throws Exception {
        IdamUserResource idamUserResource = new IdamUserResource(idamUserServiceMock);
        when(idamUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restIdamUserMockMvc = MockMvcBuilders.standaloneSetup(idamUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restIdamUserMockMvc.perform(get("/api/idam-users?eagerload=true"))
        .andExpect(status().isOk());

        verify(idamUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllIdamUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        IdamUserResource idamUserResource = new IdamUserResource(idamUserServiceMock);
            when(idamUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restIdamUserMockMvc = MockMvcBuilders.standaloneSetup(idamUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restIdamUserMockMvc.perform(get("/api/idam-users?eagerload=true"))
        .andExpect(status().isOk());

            verify(idamUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getIdamUser() throws Exception {
        // Initialize the database
        idamUserRepository.saveAndFlush(idamUser);

        // Get the idamUser
        restIdamUserMockMvc.perform(get("/api/idam-users/{id}", idamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(idamUser.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdamUser() throws Exception {
        // Get the idamUser
        restIdamUserMockMvc.perform(get("/api/idam-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdamUser() throws Exception {
        // Initialize the database
        idamUserService.save(idamUser);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIdamUserSearchRepository);

        int databaseSizeBeforeUpdate = idamUserRepository.findAll().size();

        // Update the idamUser
        IdamUser updatedIdamUser = idamUserRepository.findById(idamUser.getId()).get();
        // Disconnect from session so that the updates on updatedIdamUser are not directly saved in db
        em.detach(updatedIdamUser);
        updatedIdamUser
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .status(UPDATED_STATUS);

        restIdamUserMockMvc.perform(put("/api/idam-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIdamUser)))
            .andExpect(status().isOk());

        // Validate the IdamUser in the database
        List<IdamUser> idamUserList = idamUserRepository.findAll();
        assertThat(idamUserList).hasSize(databaseSizeBeforeUpdate);
        IdamUser testIdamUser = idamUserList.get(idamUserList.size() - 1);
        assertThat(testIdamUser.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testIdamUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testIdamUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testIdamUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testIdamUser.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the IdamUser in Elasticsearch
        verify(mockIdamUserSearchRepository, times(1)).save(testIdamUser);
    }

    @Test
    @Transactional
    public void updateNonExistingIdamUser() throws Exception {
        int databaseSizeBeforeUpdate = idamUserRepository.findAll().size();

        // Create the IdamUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdamUserMockMvc.perform(put("/api/idam-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamUser)))
            .andExpect(status().isBadRequest());

        // Validate the IdamUser in the database
        List<IdamUser> idamUserList = idamUserRepository.findAll();
        assertThat(idamUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IdamUser in Elasticsearch
        verify(mockIdamUserSearchRepository, times(0)).save(idamUser);
    }

    @Test
    @Transactional
    public void deleteIdamUser() throws Exception {
        // Initialize the database
        idamUserService.save(idamUser);

        int databaseSizeBeforeDelete = idamUserRepository.findAll().size();

        // Delete the idamUser
        restIdamUserMockMvc.perform(delete("/api/idam-users/{id}", idamUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IdamUser> idamUserList = idamUserRepository.findAll();
        assertThat(idamUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IdamUser in Elasticsearch
        verify(mockIdamUserSearchRepository, times(1)).deleteById(idamUser.getId());
    }

    @Test
    @Transactional
    public void searchIdamUser() throws Exception {
        // Initialize the database
        idamUserService.save(idamUser);
        when(mockIdamUserSearchRepository.search(queryStringQuery("id:" + idamUser.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(idamUser), PageRequest.of(0, 1), 1));
        // Search the idamUser
        restIdamUserMockMvc.perform(get("/api/_search/idam-users?query=id:" + idamUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idamUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdamUser.class);
        IdamUser idamUser1 = new IdamUser();
        idamUser1.setId(1L);
        IdamUser idamUser2 = new IdamUser();
        idamUser2.setId(idamUser1.getId());
        assertThat(idamUser1).isEqualTo(idamUser2);
        idamUser2.setId(2L);
        assertThat(idamUser1).isNotEqualTo(idamUser2);
        idamUser1.setId(null);
        assertThat(idamUser1).isNotEqualTo(idamUser2);
    }
}
