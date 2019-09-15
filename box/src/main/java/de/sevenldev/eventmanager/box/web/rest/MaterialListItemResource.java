package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.domain.MaterialListItem;
import de.sevenldev.eventmanager.box.repository.MaterialListItemRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.sevenldev.eventmanager.box.domain.MaterialListItem}.
 */
@RestController
@RequestMapping("/api")
public class MaterialListItemResource {

    private final Logger log = LoggerFactory.getLogger(MaterialListItemResource.class);

    private static final String ENTITY_NAME = "boxMaterialListItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialListItemRepository materialListItemRepository;

    public MaterialListItemResource(MaterialListItemRepository materialListItemRepository) {
        this.materialListItemRepository = materialListItemRepository;
    }

    /**
     * {@code POST  /material-list-items} : Create a new materialListItem.
     *
     * @param materialListItem the materialListItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialListItem, or with status {@code 400 (Bad Request)} if the materialListItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/material-list-items")
    public ResponseEntity<MaterialListItem> createMaterialListItem(@RequestBody MaterialListItem materialListItem) throws URISyntaxException {
        log.debug("REST request to save MaterialListItem : {}", materialListItem);
        if (materialListItem.getId() != null) {
            throw new BadRequestAlertException("A new materialListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialListItem result = materialListItemRepository.save(materialListItem);
        return ResponseEntity.created(new URI("/api/material-list-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /material-list-items} : Updates an existing materialListItem.
     *
     * @param materialListItem the materialListItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialListItem,
     * or with status {@code 400 (Bad Request)} if the materialListItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialListItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/material-list-items")
    public ResponseEntity<MaterialListItem> updateMaterialListItem(@RequestBody MaterialListItem materialListItem) throws URISyntaxException {
        log.debug("REST request to update MaterialListItem : {}", materialListItem);
        if (materialListItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialListItem result = materialListItemRepository.save(materialListItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialListItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /material-list-items} : get all the materialListItems.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialListItems in body.
     */
    @GetMapping("/material-list-items")
    public ResponseEntity<List<MaterialListItem>> getAllMaterialListItems(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of MaterialListItems");
        Page<MaterialListItem> page = materialListItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /material-list-items/:id} : get the "id" materialListItem.
     *
     * @param id the id of the materialListItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialListItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/material-list-items/{id}")
    public ResponseEntity<MaterialListItem> getMaterialListItem(@PathVariable Long id) {
        log.debug("REST request to get MaterialListItem : {}", id);
        Optional<MaterialListItem> materialListItem = materialListItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(materialListItem);
    }

    /**
     * {@code DELETE  /material-list-items/:id} : delete the "id" materialListItem.
     *
     * @param id the id of the materialListItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/material-list-items/{id}")
    public ResponseEntity<Void> deleteMaterialListItem(@PathVariable Long id) {
        log.debug("REST request to delete MaterialListItem : {}", id);
        materialListItemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
