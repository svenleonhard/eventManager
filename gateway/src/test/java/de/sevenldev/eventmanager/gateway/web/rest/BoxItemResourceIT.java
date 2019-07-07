package de.sevenldev.eventmanager.gateway.web.rest;

import de.sevenldev.eventmanager.gateway.GatewayApp;
import de.sevenldev.eventmanager.gateway.domain.BoxItem;
import de.sevenldev.eventmanager.gateway.repository.BoxItemRepository;
import de.sevenldev.eventmanager.gateway.repository.search.BoxItemSearchRepository;
import de.sevenldev.eventmanager.gateway.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.Collections;
import java.util.List;

import static de.sevenldev.eventmanager.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link BoxItemResource} REST controller.
 */
@SpringBootTest(classes = GatewayApp.class)
public class BoxItemResourceIT {

    private static final Boolean DEFAULT_TO_REPAIR = false;
    private static final Boolean UPDATED_TO_REPAIR = true;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private BoxItemRepository boxItemRepository;

    /**
     * This repository is mocked in the de.sevenldev.eventmanager.gateway.repository.search test package.
     *
     * @see de.sevenldev.eventmanager.gateway.repository.search.BoxItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private BoxItemSearchRepository mockBoxItemSearchRepository;

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

    private MockMvc restBoxItemMockMvc;

    private BoxItem boxItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoxItemResource boxItemResource = new BoxItemResource(boxItemRepository, mockBoxItemSearchRepository);
        this.restBoxItemMockMvc = MockMvcBuilders.standaloneSetup(boxItemResource)
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
    public static BoxItem createEntity(EntityManager em) {
        BoxItem boxItem = new BoxItem()
            .toRepair(DEFAULT_TO_REPAIR)
            .comment(DEFAULT_COMMENT);
        return boxItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoxItem createUpdatedEntity(EntityManager em) {
        BoxItem boxItem = new BoxItem()
            .toRepair(UPDATED_TO_REPAIR)
            .comment(UPDATED_COMMENT);
        return boxItem;
    }

    @BeforeEach
    public void initTest() {
        boxItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoxItem() throws Exception {
        int databaseSizeBeforeCreate = boxItemRepository.findAll().size();

        // Create the BoxItem
        restBoxItemMockMvc.perform(post("/api/box-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxItem)))
            .andExpect(status().isCreated());

        // Validate the BoxItem in the database
        List<BoxItem> boxItemList = boxItemRepository.findAll();
        assertThat(boxItemList).hasSize(databaseSizeBeforeCreate + 1);
        BoxItem testBoxItem = boxItemList.get(boxItemList.size() - 1);
        assertThat(testBoxItem.isToRepair()).isEqualTo(DEFAULT_TO_REPAIR);
        assertThat(testBoxItem.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the BoxItem in Elasticsearch
        verify(mockBoxItemSearchRepository, times(1)).save(testBoxItem);
    }

    @Test
    @Transactional
    public void createBoxItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxItemRepository.findAll().size();

        // Create the BoxItem with an existing ID
        boxItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoxItemMockMvc.perform(post("/api/box-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxItem)))
            .andExpect(status().isBadRequest());

        // Validate the BoxItem in the database
        List<BoxItem> boxItemList = boxItemRepository.findAll();
        assertThat(boxItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the BoxItem in Elasticsearch
        verify(mockBoxItemSearchRepository, times(0)).save(boxItem);
    }


    @Test
    @Transactional
    public void getAllBoxItems() throws Exception {
        // Initialize the database
        boxItemRepository.saveAndFlush(boxItem);

        // Get all the boxItemList
        restBoxItemMockMvc.perform(get("/api/box-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].toRepair").value(hasItem(DEFAULT_TO_REPAIR.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getBoxItem() throws Exception {
        // Initialize the database
        boxItemRepository.saveAndFlush(boxItem);

        // Get the boxItem
        restBoxItemMockMvc.perform(get("/api/box-items/{id}", boxItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boxItem.getId().intValue()))
            .andExpect(jsonPath("$.toRepair").value(DEFAULT_TO_REPAIR.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBoxItem() throws Exception {
        // Get the boxItem
        restBoxItemMockMvc.perform(get("/api/box-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoxItem() throws Exception {
        // Initialize the database
        boxItemRepository.saveAndFlush(boxItem);

        int databaseSizeBeforeUpdate = boxItemRepository.findAll().size();

        // Update the boxItem
        BoxItem updatedBoxItem = boxItemRepository.findById(boxItem.getId()).get();
        // Disconnect from session so that the updates on updatedBoxItem are not directly saved in db
        em.detach(updatedBoxItem);
        updatedBoxItem
            .toRepair(UPDATED_TO_REPAIR)
            .comment(UPDATED_COMMENT);

        restBoxItemMockMvc.perform(put("/api/box-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBoxItem)))
            .andExpect(status().isOk());

        // Validate the BoxItem in the database
        List<BoxItem> boxItemList = boxItemRepository.findAll();
        assertThat(boxItemList).hasSize(databaseSizeBeforeUpdate);
        BoxItem testBoxItem = boxItemList.get(boxItemList.size() - 1);
        assertThat(testBoxItem.isToRepair()).isEqualTo(UPDATED_TO_REPAIR);
        assertThat(testBoxItem.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the BoxItem in Elasticsearch
        verify(mockBoxItemSearchRepository, times(1)).save(testBoxItem);
    }

    @Test
    @Transactional
    public void updateNonExistingBoxItem() throws Exception {
        int databaseSizeBeforeUpdate = boxItemRepository.findAll().size();

        // Create the BoxItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoxItemMockMvc.perform(put("/api/box-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boxItem)))
            .andExpect(status().isBadRequest());

        // Validate the BoxItem in the database
        List<BoxItem> boxItemList = boxItemRepository.findAll();
        assertThat(boxItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BoxItem in Elasticsearch
        verify(mockBoxItemSearchRepository, times(0)).save(boxItem);
    }

    @Test
    @Transactional
    public void deleteBoxItem() throws Exception {
        // Initialize the database
        boxItemRepository.saveAndFlush(boxItem);

        int databaseSizeBeforeDelete = boxItemRepository.findAll().size();

        // Delete the boxItem
        restBoxItemMockMvc.perform(delete("/api/box-items/{id}", boxItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoxItem> boxItemList = boxItemRepository.findAll();
        assertThat(boxItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BoxItem in Elasticsearch
        verify(mockBoxItemSearchRepository, times(1)).deleteById(boxItem.getId());
    }

    @Test
    @Transactional
    public void searchBoxItem() throws Exception {
        // Initialize the database
        boxItemRepository.saveAndFlush(boxItem);
        when(mockBoxItemSearchRepository.search(queryStringQuery("id:" + boxItem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(boxItem), PageRequest.of(0, 1), 1));
        // Search the boxItem
        restBoxItemMockMvc.perform(get("/api/_search/box-items?query=id:" + boxItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].toRepair").value(hasItem(DEFAULT_TO_REPAIR.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoxItem.class);
        BoxItem boxItem1 = new BoxItem();
        boxItem1.setId(1L);
        BoxItem boxItem2 = new BoxItem();
        boxItem2.setId(boxItem1.getId());
        assertThat(boxItem1).isEqualTo(boxItem2);
        boxItem2.setId(2L);
        assertThat(boxItem1).isNotEqualTo(boxItem2);
        boxItem1.setId(null);
        assertThat(boxItem1).isNotEqualTo(boxItem2);
    }
}
