package uk.gov.web.rest;

import uk.gov.UsermgmtdemoApp;
import uk.gov.config.TestSecurityConfiguration;
import uk.gov.domain.IdamRole;
import uk.gov.repository.IdamRoleRepository;
import uk.gov.repository.search.IdamRoleSearchRepository;
import uk.gov.service.IdamRoleService;
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

import static uk.gov.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link IdamRoleResource} REST controller.
 */
@SpringBootTest(classes = {UsermgmtdemoApp.class, TestSecurityConfiguration.class})
public class IdamRoleResourceIT {

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    @Autowired
    private IdamRoleRepository idamRoleRepository;

    @Mock
    private IdamRoleRepository idamRoleRepositoryMock;

    @Mock
    private IdamRoleService idamRoleServiceMock;

    @Autowired
    private IdamRoleService idamRoleService;

    /**
     * This repository is mocked in the uk.gov.repository.search test package.
     *
     * @see uk.gov.repository.search.IdamRoleSearchRepositoryMockConfiguration
     */
    @Autowired
    private IdamRoleSearchRepository mockIdamRoleSearchRepository;

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

    private MockMvc restIdamRoleMockMvc;

    private IdamRole idamRole;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IdamRoleResource idamRoleResource = new IdamRoleResource(idamRoleService);
        this.restIdamRoleMockMvc = MockMvcBuilders.standaloneSetup(idamRoleResource)
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
    public static IdamRole createEntity(EntityManager em) {
        IdamRole idamRole = new IdamRole()
            .roleName(DEFAULT_ROLE_NAME);
        return idamRole;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdamRole createUpdatedEntity(EntityManager em) {
        IdamRole idamRole = new IdamRole()
            .roleName(UPDATED_ROLE_NAME);
        return idamRole;
    }

    @BeforeEach
    public void initTest() {
        idamRole = createEntity(em);
    }

    @Test
    @Transactional
    public void createIdamRole() throws Exception {
        int databaseSizeBeforeCreate = idamRoleRepository.findAll().size();

        // Create the IdamRole
        restIdamRoleMockMvc.perform(post("/api/idam-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamRole)))
            .andExpect(status().isCreated());

        // Validate the IdamRole in the database
        List<IdamRole> idamRoleList = idamRoleRepository.findAll();
        assertThat(idamRoleList).hasSize(databaseSizeBeforeCreate + 1);
        IdamRole testIdamRole = idamRoleList.get(idamRoleList.size() - 1);
        assertThat(testIdamRole.getRoleName()).isEqualTo(DEFAULT_ROLE_NAME);

        // Validate the IdamRole in Elasticsearch
        verify(mockIdamRoleSearchRepository, times(1)).save(testIdamRole);
    }

    @Test
    @Transactional
    public void createIdamRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = idamRoleRepository.findAll().size();

        // Create the IdamRole with an existing ID
        idamRole.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdamRoleMockMvc.perform(post("/api/idam-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamRole)))
            .andExpect(status().isBadRequest());

        // Validate the IdamRole in the database
        List<IdamRole> idamRoleList = idamRoleRepository.findAll();
        assertThat(idamRoleList).hasSize(databaseSizeBeforeCreate);

        // Validate the IdamRole in Elasticsearch
        verify(mockIdamRoleSearchRepository, times(0)).save(idamRole);
    }


    @Test
    @Transactional
    public void getAllIdamRoles() throws Exception {
        // Initialize the database
        idamRoleRepository.saveAndFlush(idamRole);

        // Get all the idamRoleList
        restIdamRoleMockMvc.perform(get("/api/idam-roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idamRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllIdamRolesWithEagerRelationshipsIsEnabled() throws Exception {
        IdamRoleResource idamRoleResource = new IdamRoleResource(idamRoleServiceMock);
        when(idamRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restIdamRoleMockMvc = MockMvcBuilders.standaloneSetup(idamRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restIdamRoleMockMvc.perform(get("/api/idam-roles?eagerload=true"))
        .andExpect(status().isOk());

        verify(idamRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllIdamRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        IdamRoleResource idamRoleResource = new IdamRoleResource(idamRoleServiceMock);
            when(idamRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restIdamRoleMockMvc = MockMvcBuilders.standaloneSetup(idamRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restIdamRoleMockMvc.perform(get("/api/idam-roles?eagerload=true"))
        .andExpect(status().isOk());

            verify(idamRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getIdamRole() throws Exception {
        // Initialize the database
        idamRoleRepository.saveAndFlush(idamRole);

        // Get the idamRole
        restIdamRoleMockMvc.perform(get("/api/idam-roles/{id}", idamRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(idamRole.getId().intValue()))
            .andExpect(jsonPath("$.roleName").value(DEFAULT_ROLE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIdamRole() throws Exception {
        // Get the idamRole
        restIdamRoleMockMvc.perform(get("/api/idam-roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIdamRole() throws Exception {
        // Initialize the database
        idamRoleService.save(idamRole);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIdamRoleSearchRepository);

        int databaseSizeBeforeUpdate = idamRoleRepository.findAll().size();

        // Update the idamRole
        IdamRole updatedIdamRole = idamRoleRepository.findById(idamRole.getId()).get();
        // Disconnect from session so that the updates on updatedIdamRole are not directly saved in db
        em.detach(updatedIdamRole);
        updatedIdamRole
            .roleName(UPDATED_ROLE_NAME);

        restIdamRoleMockMvc.perform(put("/api/idam-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIdamRole)))
            .andExpect(status().isOk());

        // Validate the IdamRole in the database
        List<IdamRole> idamRoleList = idamRoleRepository.findAll();
        assertThat(idamRoleList).hasSize(databaseSizeBeforeUpdate);
        IdamRole testIdamRole = idamRoleList.get(idamRoleList.size() - 1);
        assertThat(testIdamRole.getRoleName()).isEqualTo(UPDATED_ROLE_NAME);

        // Validate the IdamRole in Elasticsearch
        verify(mockIdamRoleSearchRepository, times(1)).save(testIdamRole);
    }

    @Test
    @Transactional
    public void updateNonExistingIdamRole() throws Exception {
        int databaseSizeBeforeUpdate = idamRoleRepository.findAll().size();

        // Create the IdamRole

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdamRoleMockMvc.perform(put("/api/idam-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(idamRole)))
            .andExpect(status().isBadRequest());

        // Validate the IdamRole in the database
        List<IdamRole> idamRoleList = idamRoleRepository.findAll();
        assertThat(idamRoleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IdamRole in Elasticsearch
        verify(mockIdamRoleSearchRepository, times(0)).save(idamRole);
    }

    @Test
    @Transactional
    public void deleteIdamRole() throws Exception {
        // Initialize the database
        idamRoleService.save(idamRole);

        int databaseSizeBeforeDelete = idamRoleRepository.findAll().size();

        // Delete the idamRole
        restIdamRoleMockMvc.perform(delete("/api/idam-roles/{id}", idamRole.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IdamRole> idamRoleList = idamRoleRepository.findAll();
        assertThat(idamRoleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IdamRole in Elasticsearch
        verify(mockIdamRoleSearchRepository, times(1)).deleteById(idamRole.getId());
    }

    @Test
    @Transactional
    public void searchIdamRole() throws Exception {
        // Initialize the database
        idamRoleService.save(idamRole);
        when(mockIdamRoleSearchRepository.search(queryStringQuery("id:" + idamRole.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(idamRole), PageRequest.of(0, 1), 1));
        // Search the idamRole
        restIdamRoleMockMvc.perform(get("/api/_search/idam-roles?query=id:" + idamRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(idamRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].roleName").value(hasItem(DEFAULT_ROLE_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdamRole.class);
        IdamRole idamRole1 = new IdamRole();
        idamRole1.setId(1L);
        IdamRole idamRole2 = new IdamRole();
        idamRole2.setId(idamRole1.getId());
        assertThat(idamRole1).isEqualTo(idamRole2);
        idamRole2.setId(2L);
        assertThat(idamRole1).isNotEqualTo(idamRole2);
        idamRole1.setId(null);
        assertThat(idamRole1).isNotEqualTo(idamRole2);
    }
}
