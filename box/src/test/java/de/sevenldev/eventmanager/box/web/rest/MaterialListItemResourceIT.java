package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.BoxApp;
import de.sevenldev.eventmanager.box.domain.MaterialListItem;
import de.sevenldev.eventmanager.box.repository.MaterialListItemRepository;
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
 * Integration tests for the {@Link MaterialListItemResource} REST controller.
 */
@SpringBootTest(classes = BoxApp.class)
public class MaterialListItemResourceIT {

    private static final Boolean DEFAULT_CHECKED_IN = false;
    private static final Boolean UPDATED_CHECKED_IN = true;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private MaterialListItemRepository materialListItemRepository;

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

    private MockMvc restMaterialListItemMockMvc;

    private MaterialListItem materialListItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialListItemResource materialListItemResource = new MaterialListItemResource(materialListItemRepository);
        this.restMaterialListItemMockMvc = MockMvcBuilders.standaloneSetup(materialListItemResource)
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
    public static MaterialListItem createEntity(EntityManager em) {
        MaterialListItem materialListItem = new MaterialListItem()
            .checkedIn(DEFAULT_CHECKED_IN)
            .comment(DEFAULT_COMMENT);
        return materialListItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialListItem createUpdatedEntity(EntityManager em) {
        MaterialListItem materialListItem = new MaterialListItem()
            .checkedIn(UPDATED_CHECKED_IN)
            .comment(UPDATED_COMMENT);
        return materialListItem;
    }

    @BeforeEach
    public void initTest() {
        materialListItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialListItem() throws Exception {
        int databaseSizeBeforeCreate = materialListItemRepository.findAll().size();

        // Create the MaterialListItem
        restMaterialListItemMockMvc.perform(post("/api/material-list-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialListItem)))
            .andExpect(status().isCreated());

        // Validate the MaterialListItem in the database
        List<MaterialListItem> materialListItemList = materialListItemRepository.findAll();
        assertThat(materialListItemList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialListItem testMaterialListItem = materialListItemList.get(materialListItemList.size() - 1);
        assertThat(testMaterialListItem.isCheckedIn()).isEqualTo(DEFAULT_CHECKED_IN);
        assertThat(testMaterialListItem.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createMaterialListItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialListItemRepository.findAll().size();

        // Create the MaterialListItem with an existing ID
        materialListItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialListItemMockMvc.perform(post("/api/material-list-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialListItem)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialListItem in the database
        List<MaterialListItem> materialListItemList = materialListItemRepository.findAll();
        assertThat(materialListItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMaterialListItems() throws Exception {
        // Initialize the database
        materialListItemRepository.saveAndFlush(materialListItem);

        // Get all the materialListItemList
        restMaterialListItemMockMvc.perform(get("/api/material-list-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].checkedIn").value(hasItem(DEFAULT_CHECKED_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getMaterialListItem() throws Exception {
        // Initialize the database
        materialListItemRepository.saveAndFlush(materialListItem);

        // Get the materialListItem
        restMaterialListItemMockMvc.perform(get("/api/material-list-items/{id}", materialListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialListItem.getId().intValue()))
            .andExpect(jsonPath("$.checkedIn").value(DEFAULT_CHECKED_IN.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialListItem() throws Exception {
        // Get the materialListItem
        restMaterialListItemMockMvc.perform(get("/api/material-list-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialListItem() throws Exception {
        // Initialize the database
        materialListItemRepository.saveAndFlush(materialListItem);

        int databaseSizeBeforeUpdate = materialListItemRepository.findAll().size();

        // Update the materialListItem
        MaterialListItem updatedMaterialListItem = materialListItemRepository.findById(materialListItem.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialListItem are not directly saved in db
        em.detach(updatedMaterialListItem);
        updatedMaterialListItem
            .checkedIn(UPDATED_CHECKED_IN)
            .comment(UPDATED_COMMENT);

        restMaterialListItemMockMvc.perform(put("/api/material-list-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaterialListItem)))
            .andExpect(status().isOk());

        // Validate the MaterialListItem in the database
        List<MaterialListItem> materialListItemList = materialListItemRepository.findAll();
        assertThat(materialListItemList).hasSize(databaseSizeBeforeUpdate);
        MaterialListItem testMaterialListItem = materialListItemList.get(materialListItemList.size() - 1);
        assertThat(testMaterialListItem.isCheckedIn()).isEqualTo(UPDATED_CHECKED_IN);
        assertThat(testMaterialListItem.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialListItem() throws Exception {
        int databaseSizeBeforeUpdate = materialListItemRepository.findAll().size();

        // Create the MaterialListItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialListItemMockMvc.perform(put("/api/material-list-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialListItem)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialListItem in the database
        List<MaterialListItem> materialListItemList = materialListItemRepository.findAll();
        assertThat(materialListItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterialListItem() throws Exception {
        // Initialize the database
        materialListItemRepository.saveAndFlush(materialListItem);

        int databaseSizeBeforeDelete = materialListItemRepository.findAll().size();

        // Delete the materialListItem
        restMaterialListItemMockMvc.perform(delete("/api/material-list-items/{id}", materialListItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialListItem> materialListItemList = materialListItemRepository.findAll();
        assertThat(materialListItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialListItem.class);
        MaterialListItem materialListItem1 = new MaterialListItem();
        materialListItem1.setId(1L);
        MaterialListItem materialListItem2 = new MaterialListItem();
        materialListItem2.setId(materialListItem1.getId());
        assertThat(materialListItem1).isEqualTo(materialListItem2);
        materialListItem2.setId(2L);
        assertThat(materialListItem1).isNotEqualTo(materialListItem2);
        materialListItem1.setId(null);
        assertThat(materialListItem1).isNotEqualTo(materialListItem2);
    }
}
