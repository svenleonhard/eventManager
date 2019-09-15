package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.BoxApp;
import de.sevenldev.eventmanager.box.domain.MaterialList;
import de.sevenldev.eventmanager.box.repository.MaterialListRepository;
import de.sevenldev.eventmanager.box.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static de.sevenldev.eventmanager.box.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MaterialListResource} REST controller.
 */
@SpringBootTest(classes = BoxApp.class)
public class MaterialListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_EVENT_START = 1;
    private static final Integer UPDATED_EVENT_START = 2;

    private static final Integer DEFAULT_EVENT_END = 1;
    private static final Integer UPDATED_EVENT_END = 2;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPEN = false;
    private static final Boolean UPDATED_OPEN = true;

    @Autowired
    private MaterialListRepository materialListRepository;

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

    private MockMvc restMaterialListMockMvc;

    private MaterialList materialList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialListResource materialListResource = new MaterialListResource(materialListRepository);
        this.restMaterialListMockMvc = MockMvcBuilders.standaloneSetup(materialListResource)
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
    public static MaterialList createEntity(EntityManager em) {
        MaterialList materialList = new MaterialList()
            .name(DEFAULT_NAME)
            .eventStart(DEFAULT_EVENT_START)
            .eventEnd(DEFAULT_EVENT_END)
            .location(DEFAULT_LOCATION)
            .open(DEFAULT_OPEN);
        return materialList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialList createUpdatedEntity(EntityManager em) {
        MaterialList materialList = new MaterialList()
            .name(UPDATED_NAME)
            .eventStart(UPDATED_EVENT_START)
            .eventEnd(UPDATED_EVENT_END)
            .location(UPDATED_LOCATION)
            .open(UPDATED_OPEN);
        return materialList;
    }

    @BeforeEach
    public void initTest() {
        materialList = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialList() throws Exception {
        int databaseSizeBeforeCreate = materialListRepository.findAll().size();

        // Create the MaterialList
        restMaterialListMockMvc.perform(post("/api/material-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialList)))
            .andExpect(status().isCreated());

        // Validate the MaterialList in the database
        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialList testMaterialList = materialListList.get(materialListList.size() - 1);
        assertThat(testMaterialList.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterialList.getEventStart()).isEqualTo(DEFAULT_EVENT_START);
        assertThat(testMaterialList.getEventEnd()).isEqualTo(DEFAULT_EVENT_END);
        assertThat(testMaterialList.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testMaterialList.isOpen()).isEqualTo(DEFAULT_OPEN);
    }

    @Test
    @Transactional
    public void createMaterialListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialListRepository.findAll().size();

        // Create the MaterialList with an existing ID
        materialList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialListMockMvc.perform(post("/api/material-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialList)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialList in the database
        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialListRepository.findAll().size();
        // set the field null
        materialList.setName(null);

        // Create the MaterialList, which fails.

        restMaterialListMockMvc.perform(post("/api/material-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialList)))
            .andExpect(status().isBadRequest());

        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterialLists() throws Exception {
        // Initialize the database
        materialListRepository.saveAndFlush(materialList);

        // Get all the materialListList
        restMaterialListMockMvc.perform(get("/api/material-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].eventStart").value(hasItem(DEFAULT_EVENT_START)))
            .andExpect(jsonPath("$.[*].eventEnd").value(hasItem(DEFAULT_EVENT_END)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMaterialList() throws Exception {
        // Initialize the database
        materialListRepository.saveAndFlush(materialList);

        // Get the materialList
        restMaterialListMockMvc.perform(get("/api/material-lists/{id}", materialList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.eventStart").value(DEFAULT_EVENT_START))
            .andExpect(jsonPath("$.eventEnd").value(DEFAULT_EVENT_END))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialList() throws Exception {
        // Get the materialList
        restMaterialListMockMvc.perform(get("/api/material-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialList() throws Exception {
        // Initialize the database
        materialListRepository.saveAndFlush(materialList);

        int databaseSizeBeforeUpdate = materialListRepository.findAll().size();

        // Update the materialList
        MaterialList updatedMaterialList = materialListRepository.findById(materialList.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialList are not directly saved in db
        em.detach(updatedMaterialList);
        updatedMaterialList
            .name(UPDATED_NAME)
            .eventStart(UPDATED_EVENT_START)
            .eventEnd(UPDATED_EVENT_END)
            .location(UPDATED_LOCATION)
            .open(UPDATED_OPEN);

        restMaterialListMockMvc.perform(put("/api/material-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterialList)))
            .andExpect(status().isOk());

        // Validate the MaterialList in the database
        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeUpdate);
        MaterialList testMaterialList = materialListList.get(materialListList.size() - 1);
        assertThat(testMaterialList.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterialList.getEventStart()).isEqualTo(UPDATED_EVENT_START);
        assertThat(testMaterialList.getEventEnd()).isEqualTo(UPDATED_EVENT_END);
        assertThat(testMaterialList.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testMaterialList.isOpen()).isEqualTo(UPDATED_OPEN);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialList() throws Exception {
        int databaseSizeBeforeUpdate = materialListRepository.findAll().size();

        // Create the MaterialList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialListMockMvc.perform(put("/api/material-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialList)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialList in the database
        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterialList() throws Exception {
        // Initialize the database
        materialListRepository.saveAndFlush(materialList);

        int databaseSizeBeforeDelete = materialListRepository.findAll().size();

        // Delete the materialList
        restMaterialListMockMvc.perform(delete("/api/material-lists/{id}", materialList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialList> materialListList = materialListRepository.findAll();
        assertThat(materialListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialList.class);
        MaterialList materialList1 = new MaterialList();
        materialList1.setId(1L);
        MaterialList materialList2 = new MaterialList();
        materialList2.setId(materialList1.getId());
        assertThat(materialList1).isEqualTo(materialList2);
        materialList2.setId(2L);
        assertThat(materialList1).isNotEqualTo(materialList2);
        materialList1.setId(null);
        assertThat(materialList1).isNotEqualTo(materialList2);
    }
}
