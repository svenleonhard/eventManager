package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.BoxApp;
import de.sevenldev.eventmanager.box.domain.Box;
import de.sevenldev.eventmanager.box.repository.BoxRepository;
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
import org.springframework.validation.Validator;


import java.util.List;

import static de.sevenldev.eventmanager.box.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link BoxResource} REST controller.
 */
@SpringBootTest(classes = BoxApp.class)
public class BoxResourceIT {

    private static final String DEFAULT_EXTENED_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTENED_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restBoxMockMvc;

    private Box box;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoxResource boxResource = new BoxResource(boxRepository);
        this.restBoxMockMvc = MockMvcBuilders.standaloneSetup(boxResource)
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
    public static Box createEntity() {
        Box box = new Box()
            .extenedId(DEFAULT_EXTENED_ID)
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION);
        return box;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Box createUpdatedEntity() {
        Box box = new Box()
            .extenedId(UPDATED_EXTENED_ID)
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION);
        return box;
    }

    @BeforeEach
    public void initTest() {
        boxRepository.deleteAll();
        box = createEntity();
    }

    @Test
    public void createBox() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box
        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isCreated());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate + 1);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getExtenedId()).isEqualTo(DEFAULT_EXTENED_ID);
        assertThat(testBox.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBox.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testBox.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    public void createBoxWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxRepository.findAll().size();

        // Create the Box with an existing ID
        box.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = boxRepository.findAll().size();
        // set the field null
        box.setName(null);

        // Create the Box, which fails.

        restBoxMockMvc.perform(post("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllBoxes() throws Exception {
        // Initialize the database
        boxRepository.save(box);

        // Get all the boxList
        restBoxMockMvc.perform(get("/api/boxes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(box.getId())))
            .andExpect(jsonPath("$.[*].extenedId").value(hasItem(DEFAULT_EXTENED_ID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    public void getBox() throws Exception {
        // Initialize the database
        boxRepository.save(box);

        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", box.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(box.getId()))
            .andExpect(jsonPath("$.extenedId").value(DEFAULT_EXTENED_ID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    public void getNonExistingBox() throws Exception {
        // Get the box
        restBoxMockMvc.perform(get("/api/boxes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBox() throws Exception {
        // Initialize the database
        boxRepository.save(box);

        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Update the box
        Box updatedBox = boxRepository.findById(box.getId()).get();
        updatedBox
            .extenedId(UPDATED_EXTENED_ID)
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION);

        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBox)))
            .andExpect(status().isOk());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
        Box testBox = boxList.get(boxList.size() - 1);
        assertThat(testBox.getExtenedId()).isEqualTo(UPDATED_EXTENED_ID);
        assertThat(testBox.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBox.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testBox.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    public void updateNonExistingBox() throws Exception {
        int databaseSizeBeforeUpdate = boxRepository.findAll().size();

        // Create the Box

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoxMockMvc.perform(put("/api/boxes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(box)))
            .andExpect(status().isBadRequest());

        // Validate the Box in the database
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBox() throws Exception {
        // Initialize the database
        boxRepository.save(box);

        int databaseSizeBeforeDelete = boxRepository.findAll().size();

        // Delete the box
        restBoxMockMvc.perform(delete("/api/boxes/{id}", box.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Box> boxList = boxRepository.findAll();
        assertThat(boxList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Box.class);
        Box box1 = new Box();
        box1.setId("id1");
        Box box2 = new Box();
        box2.setId(box1.getId());
        assertThat(box1).isEqualTo(box2);
        box2.setId("id2");
        assertThat(box1).isNotEqualTo(box2);
        box1.setId(null);
        assertThat(box1).isNotEqualTo(box2);
    }
}
