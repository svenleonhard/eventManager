package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.domain.Box;
import de.sevenldev.eventmanager.box.repository.BoxRepository;
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
 * REST controller for managing {@link de.sevenldev.eventmanager.box.domain.Box}.
 */
@RestController
@RequestMapping("/api")
public class BoxResource {

    private final Logger log = LoggerFactory.getLogger(BoxResource.class);

    private static final String ENTITY_NAME = "boxBox";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoxRepository boxRepository;

    public BoxResource(BoxRepository boxRepository) {
        this.boxRepository = boxRepository;
    }

    /**
     * {@code POST  /boxes} : Create a new box.
     *
     * @param box the box to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new box, or with status {@code 400 (Bad Request)} if the box has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boxes")
    public ResponseEntity<Box> createBox(@Valid @RequestBody Box box) throws URISyntaxException {
        log.debug("REST request to save Box : {}", box);
        if (box.getId() != null) {
            throw new BadRequestAlertException("A new box cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Box result = boxRepository.save(box);
        return ResponseEntity.created(new URI("/api/boxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boxes} : Updates an existing box.
     *
     * @param box the box to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated box,
     * or with status {@code 400 (Bad Request)} if the box is not valid,
     * or with status {@code 500 (Internal Server Error)} if the box couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boxes")
    public ResponseEntity<Box> updateBox(@Valid @RequestBody Box box) throws URISyntaxException {
        log.debug("REST request to update Box : {}", box);
        if (box.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Box result = boxRepository.save(box);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, box.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /boxes} : get all the boxes.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boxes in body.
     */
    @GetMapping("/boxes")
    public ResponseEntity<List<Box>> getAllBoxes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Boxes");
        Page<Box> page = boxRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /boxes/:id} : get the "id" box.
     *
     * @param id the id of the box to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the box, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boxes/{id}")
    public ResponseEntity<Box> getBox(@PathVariable String id) {
        log.debug("REST request to get Box : {}", id);
        Optional<Box> box = boxRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(box);
    }

    /**
     * {@code DELETE  /boxes/:id} : delete the "id" box.
     *
     * @param id the id of the box to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boxes/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable String id) {
        log.debug("REST request to delete Box : {}", id);
        boxRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
