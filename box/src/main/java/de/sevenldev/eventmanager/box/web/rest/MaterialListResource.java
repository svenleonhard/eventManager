package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.domain.MaterialList;
import de.sevenldev.eventmanager.box.repository.MaterialListRepository;
import de.sevenldev.eventmanager.box.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.sevenldev.eventmanager.box.domain.MaterialList}.
 */
@RestController
@RequestMapping("/api")
public class MaterialListResource {

    private final Logger log = LoggerFactory.getLogger(MaterialListResource.class);

    private static final String ENTITY_NAME = "boxMaterialList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialListRepository materialListRepository;

    public MaterialListResource(MaterialListRepository materialListRepository) {
        this.materialListRepository = materialListRepository;
    }

    /**
     * {@code POST  /material-lists} : Create a new materialList.
     *
     * @param materialList the materialList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialList, or with status {@code 400 (Bad Request)} if the materialList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/material-lists")
    public ResponseEntity<MaterialList> createMaterialList(@Valid @RequestBody MaterialList materialList) throws URISyntaxException {
        log.debug("REST request to save MaterialList : {}", materialList);
        if (materialList.getId() != null) {
            throw new BadRequestAlertException("A new materialList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialList result = materialListRepository.save(materialList);
        return ResponseEntity.created(new URI("/api/material-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /material-lists} : Updates an existing materialList.
     *
     * @param materialList the materialList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialList,
     * or with status {@code 400 (Bad Request)} if the materialList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/material-lists")
    public ResponseEntity<MaterialList> updateMaterialList(@Valid @RequestBody MaterialList materialList) throws URISyntaxException {
        log.debug("REST request to update MaterialList : {}", materialList);
        if (materialList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialList result = materialListRepository.save(materialList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialList.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /material-lists} : get all the materialLists.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialLists in body.
     */
    @GetMapping("/material-lists")
    public ResponseEntity<List<MaterialList>> getAllMaterialLists(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of MaterialLists");
        Page<MaterialList> page = materialListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-lists/:id} : get the "id" materialList.
     *
     * @param id the id of the materialList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/material-lists/{id}")
    public ResponseEntity<MaterialList> getMaterialList(@PathVariable Long id) {
        log.debug("REST request to get MaterialList : {}", id);
        Optional<MaterialList> materialList = materialListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(materialList);
    }

    /**
     * {@code DELETE  /material-lists/:id} : delete the "id" materialList.
     *
     * @param id the id of the materialList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/material-lists/{id}")
    public ResponseEntity<Void> deleteMaterialList(@PathVariable Long id) {
        log.debug("REST request to delete MaterialList : {}", id);
        materialListRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
