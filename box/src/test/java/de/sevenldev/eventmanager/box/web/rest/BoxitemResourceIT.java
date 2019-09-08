package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.BoxApp;
import de.sevenldev.eventmanager.box.domain.Boxitem;
import de.sevenldev.eventmanager.box.domain.Box;
import de.sevenldev.eventmanager.box.domain.Item;
import de.sevenldev.eventmanager.box.repository.BoxitemRepository;
import de.sevenldev.eventmanager.box.service.BoxitemService;
import de.sevenldev.eventmanager.box.web.rest.errors.ExceptionTranslator;
import de.sevenldev.eventmanager.box.service.dto.BoxitemCriteria;
import de.sevenldev.eventmanager.box.service.BoxitemQueryService;

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
 * Integration tests for the {@link BoxitemResource} REST controller.
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
    private BoxitemService boxitemService;

    @Autowired
    private BoxitemQueryService boxitemQueryService;

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

    private MockMvc restBoxitemMockMvc;

    private Boxitem boxitem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoxitemResource boxitemResource = new BoxitemResource(boxitemService, boxitemQueryService);
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
    public static Boxitem createEntity(EntityManager em) {
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
    public static Boxitem createUpdatedEntity(EntityManager em) {
        Boxitem boxitem = new Boxitem()
            .toRepair(UPDATED_TO_REPAIR)
            .comment(UPDATED_COMMENT);
        return boxitem;
    }

    @BeforeEach
    public void initTest() {
        boxitem = createEntity(em);
    }

    @Test
    @Transactional
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
    @Transactional
    public void createBoxitemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boxitemRepository.findAll().size();

        // Create the Boxitem with an existing ID
        boxitem.setId(1L);

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
    @Transactional
    public void getAllBoxitems() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList
        restBoxitemMockMvc.perform(get("/api/boxitems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].toRepair").value(hasItem(DEFAULT_TO_REPAIR.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getBoxitem() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get the boxitem
        restBoxitemMockMvc.perform(get("/api/boxitems/{id}", boxitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boxitem.getId().intValue()))
            .andExpect(jsonPath("$.toRepair").value(DEFAULT_TO_REPAIR.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllBoxitemsByToRepairIsEqualToSomething() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where toRepair equals to DEFAULT_TO_REPAIR
        defaultBoxitemShouldBeFound("toRepair.equals=" + DEFAULT_TO_REPAIR);

        // Get all the boxitemList where toRepair equals to UPDATED_TO_REPAIR
        defaultBoxitemShouldNotBeFound("toRepair.equals=" + UPDATED_TO_REPAIR);
    }

    @Test
    @Transactional
    public void getAllBoxitemsByToRepairIsInShouldWork() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where toRepair in DEFAULT_TO_REPAIR or UPDATED_TO_REPAIR
        defaultBoxitemShouldBeFound("toRepair.in=" + DEFAULT_TO_REPAIR + "," + UPDATED_TO_REPAIR);

        // Get all the boxitemList where toRepair equals to UPDATED_TO_REPAIR
        defaultBoxitemShouldNotBeFound("toRepair.in=" + UPDATED_TO_REPAIR);
    }

    @Test
    @Transactional
    public void getAllBoxitemsByToRepairIsNullOrNotNull() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where toRepair is not null
        defaultBoxitemShouldBeFound("toRepair.specified=true");

        // Get all the boxitemList where toRepair is null
        defaultBoxitemShouldNotBeFound("toRepair.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoxitemsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where comment equals to DEFAULT_COMMENT
        defaultBoxitemShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the boxitemList where comment equals to UPDATED_COMMENT
        defaultBoxitemShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllBoxitemsByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultBoxitemShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the boxitemList where comment equals to UPDATED_COMMENT
        defaultBoxitemShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllBoxitemsByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);

        // Get all the boxitemList where comment is not null
        defaultBoxitemShouldBeFound("comment.specified=true");

        // Get all the boxitemList where comment is null
        defaultBoxitemShouldNotBeFound("comment.specified=false");
    }

    @Test
    @Transactional
    public void getAllBoxitemsByBoxIsEqualToSomething() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);
        Box box = BoxResourceIT.createEntity(em);
        em.persist(box);
        em.flush();
        boxitem.setBox(box);
        boxitemRepository.saveAndFlush(boxitem);
        Long boxId = box.getId();

        // Get all the boxitemList where box equals to boxId
        defaultBoxitemShouldBeFound("boxId.equals=" + boxId);

        // Get all the boxitemList where box equals to boxId + 1
        defaultBoxitemShouldNotBeFound("boxId.equals=" + (boxId + 1));
    }


    @Test
    @Transactional
    public void getAllBoxitemsByItemIsEqualToSomething() throws Exception {
        // Initialize the database
        boxitemRepository.saveAndFlush(boxitem);
        Item item = ItemResourceIT.createEntity(em);
        em.persist(item);
        em.flush();
        boxitem.setItem(item);
        boxitemRepository.saveAndFlush(boxitem);
        Long itemId = item.getId();

        // Get all the boxitemList where item equals to itemId
        defaultBoxitemShouldBeFound("itemId.equals=" + itemId);

        // Get all the boxitemList where item equals to itemId + 1
        defaultBoxitemShouldNotBeFound("itemId.equals=" + (itemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoxitemShouldBeFound(String filter) throws Exception {
        restBoxitemMockMvc.perform(get("/api/boxitems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boxitem.getId().intValue())))
            .andExpect(jsonPath("$.[*].toRepair").value(hasItem(DEFAULT_TO_REPAIR.booleanValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));

        // Check, that the count call also returns 1
        restBoxitemMockMvc.perform(get("/api/boxitems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoxitemShouldNotBeFound(String filter) throws Exception {
        restBoxitemMockMvc.perform(get("/api/boxitems?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoxitemMockMvc.perform(get("/api/boxitems/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBoxitem() throws Exception {
        // Get the boxitem
        restBoxitemMockMvc.perform(get("/api/boxitems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoxitem() throws Exception {
        // Initialize the database
        boxitemService.save(boxitem);

        int databaseSizeBeforeUpdate = boxitemRepository.findAll().size();

        // Update the boxitem
        Boxitem updatedBoxitem = boxitemRepository.findById(boxitem.getId()).get();
        // Disconnect from session so that the updates on updatedBoxitem are not directly saved in db
        em.detach(updatedBoxitem);
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
    @Transactional
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
    @Transactional
    public void deleteBoxitem() throws Exception {
        // Initialize the database
        boxitemService.save(boxitem);

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
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boxitem.class);
        Boxitem boxitem1 = new Boxitem();
        boxitem1.setId(1L);
        Boxitem boxitem2 = new Boxitem();
        boxitem2.setId(boxitem1.getId());
        assertThat(boxitem1).isEqualTo(boxitem2);
        boxitem2.setId(2L);
        assertThat(boxitem1).isNotEqualTo(boxitem2);
        boxitem1.setId(null);
        assertThat(boxitem1).isNotEqualTo(boxitem2);
    }
}
