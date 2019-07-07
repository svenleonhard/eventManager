package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.BoxApp;
import de.sevenldev.eventmanager.box.domain.Boxitem;
import de.sevenldev.eventmanager.box.repository.BoxitemRepository;
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
 * Integration tests for the {@Link BoxitemResource} REST controller.
 */
@SpringBootTest(classes = BoxApp.class)
public class BoxitemResourceIT {

    private static final Boolean DEFAULT_TO_REPAIR = false;
    private static final Boolean UPDATED_TO_REPAIR = true;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private BoxitemRepository boxitemRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restBoxitemMockMvc;

    private Boxitem boxitem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoxitemResource boxitemResource = new BoxitemResource(boxitemRepository);
        this.restBoxitemMockMvc = MockMvcBuilders.standaloneSetup(boxitemResource)
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
    public static Boxitem createEntity() {
        Boxitem boxitem = new Boxitem()
            .toRepair(DEFAULT_TO_REPAIR)
            .comment(DEFAULT_COMMENT);
        return boxitem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boxitem createUpdatedEntity() {
        Boxitem boxitem = new Boxitem()
            .toRepair(UPDATED_TO_REPAIR)
            .comment(UPDATED_COMMENT);
        return boxitem;
    }

    @BeforeEach
    public void initTest() {
        boxitemRepository.deleteAll();
        boxitem = createEntity();
    }

    @Test
    public void createBoxitem() throws Exception {
        int databaseSizeBeforeCreate = boxitemRepository.findAll().size();

        // Create the Boxitem
        restBoxitemMockMvc.perform(post("/api/boxitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxitem)))
            .andExpect(status().isCreated());

        // Validate the Boxitem in the database
        List<Boxitem> boxitemList = boxitemRepository.findAll();
        assertThat(boxitemList).hasSize(databaseSizeBeforeCreate + 1);
        Boxitem testBoxitem = boxitemList.get(boxitemList.size() - 1);
        assertThat(testBoxitem.isToRepair()).isEqualTo(DEFAULT_TO_REPAIR);
        assertThat(testBoxitem.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    public void createBoxitemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxitemRepository.findAll().size();

        // Create the Boxitem with an existing ID
        boxitem.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxitemMockMvc.perform(post("/api/boxitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxitem)))
            .andExpect(status().isBadRequest());

        // Validate the Boxitem in the database
        List<Boxitem> boxitemList = boxitemRepository.findAll();
        assertThat(boxitemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllBoxitems() throws Exception {
        // Initialize the database
        boxitemRepository.save(boxitem);

        // Get all the boxitemList
        restBoxitemMockMvc.perform(get("/api/boxitems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxitem.getId())))
            .andExpect(jsonPath("$.[*].toRepair").value(hasItem(DEFAULT_TO_REPAIR.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    public void getBoxitem() throws Exception {
        // Initialize the database
        boxitemRepository.save(boxitem);

        // Get the boxitem
        restBoxitemMockMvc.perform(get("/api/boxitems/{id}", boxitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boxitem.getId()))
            .andExpect(jsonPath("$.toRepair").value(DEFAULT_TO_REPAIR.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    public void getNonExistingBoxitem() throws Exception {
        // Get the boxitem
        restBoxitemMockMvc.perform(get("/api/boxitems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBoxitem() throws Exception {
        // Initialize the database
        boxitemRepository.save(boxitem);

        int databaseSizeBeforeUpdate = boxitemRepository.findAll().size();

        // Update the boxitem
        Boxitem updatedBoxitem = boxitemRepository.findById(boxitem.getId()).get();
        updatedBoxitem
            .toRepair(UPDATED_TO_REPAIR)
            .comment(UPDATED_COMMENT);

        restBoxitemMockMvc.perform(put("/api/boxitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoxitem)))
            .andExpect(status().isOk());

        // Validate the Boxitem in the database
        List<Boxitem> boxitemList = boxitemRepository.findAll();
        assertThat(boxitemList).hasSize(databaseSizeBeforeUpdate);
        Boxitem testBoxitem = boxitemList.get(boxitemList.size() - 1);
        assertThat(testBoxitem.isToRepair()).isEqualTo(UPDATED_TO_REPAIR);
        assertThat(testBoxitem.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    public void updateNonExistingBoxitem() throws Exception {
        int databaseSizeBeforeUpdate = boxitemRepository.findAll().size();

        // Create the Boxitem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoxitemMockMvc.perform(put("/api/boxitems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxitem)))
            .andExpect(status().isBadRequest());

        // Validate the Boxitem in the database
        List<Boxitem> boxitemList = boxitemRepository.findAll();
        assertThat(boxitemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBoxitem() throws Exception {
        // Initialize the database
        boxitemRepository.save(boxitem);

        int databaseSizeBeforeDelete = boxitemRepository.findAll().size();

        // Delete the boxitem
        restBoxitemMockMvc.perform(delete("/api/boxitems/{id}", boxitem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Boxitem> boxitemList = boxitemRepository.findAll();
        assertThat(boxitemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boxitem.class);
        Boxitem boxitem1 = new Boxitem();
        boxitem1.setId("id1");
        Boxitem boxitem2 = new Boxitem();
        boxitem2.setId(boxitem1.getId());
        assertThat(boxitem1).isEqualTo(boxitem2);
        boxitem2.setId("id2");
        assertThat(boxitem1).isNotEqualTo(boxitem2);
        boxitem1.setId(null);
        assertThat(boxitem1).isNotEqualTo(boxitem2);
    }
}
