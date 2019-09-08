package de.sevenldev.eventmanager.box.web.rest;

import de.sevenldev.eventmanager.box.domain.Boxitem;
import de.sevenldev.eventmanager.box.service.BoxitemService;
import de.sevenldev.eventmanager.box.web.rest.errors.BadRequestAlertException;
import de.sevenldev.eventmanager.box.service.dto.BoxitemCriteria;
import de.sevenldev.eventmanager.box.service.BoxitemQueryService;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link de.sevenldev.eventmanager.box.domain.Boxitem}.
 */
@RestController
@RequestMapping("/api")
public class BoxitemResource {

    private final Logger log = LoggerFactory.getLogger(BoxitemResource.class);

    private static final String ENTITY_NAME = "boxBoxitem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoxitemService boxitemService;

    private final BoxitemQueryService boxitemQueryService;

    public BoxitemResource(BoxitemService boxitemService, BoxitemQueryService boxitemQueryService) {
        this.boxitemService = boxitemService;
        this.boxitemQueryService = boxitemQueryService;
    }

    /**
     * {@code POST  /boxitems} : Create a new boxitem.
     *
     * @param boxitem the boxitem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boxitem, or with status {@code 400 (Bad Request)} if the boxitem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boxitems")
    public ResponseEntity<Boxitem> createBoxitem(@RequestBody Boxitem boxitem) throws URISyntaxException {
        log.debug("REST request to save Boxitem : {}", boxitem);
        if (boxitem.getId() != null) {
            throw new BadRequestAlertException("A new boxitem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Boxitem result = boxitemService.save(boxitem);
        return ResponseEntity.created(new URI("/api/boxitems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boxitems} : Updates an existing boxitem.
     *
     * @param boxitem the boxitem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boxitem,
     * or with status {@code 400 (Bad Request)} if the boxitem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boxitem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boxitems")
    public ResponseEntity<Boxitem> updateBoxitem(@RequestBody Boxitem boxitem) throws URISyntaxException {
        log.debug("REST request to update Boxitem : {}", boxitem);
        if (boxitem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Boxitem result = boxitemService.save(boxitem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boxitem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /boxitems} : get all the boxitems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boxitems in body.
     */
    @GetMapping("/boxitems")
    public ResponseEntity<List<Boxitem>> getAllBoxitems(BoxitemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Boxitems by criteria: {}", criteria);
        Page<Boxitem> page = boxitemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /boxitems/count} : count all the boxitems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/boxitems/count")
    public ResponseEntity<Long> countBoxitems(BoxitemCriteria criteria) {
        log.debug("REST request to count Boxitems by criteria: {}", criteria);
        return ResponseEntity.ok().body(boxitemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /boxitems/:id} : get the "id" boxitem.
     *
     * @param id the id of the boxitem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boxitem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boxitems/{id}")
    public ResponseEntity<Boxitem> getBoxitem(@PathVariable Long id) {
        log.debug("REST request to get Boxitem : {}", id);
        Optional<Boxitem> boxitem = boxitemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boxitem);
    }

    /**
     * {@code DELETE  /boxitems/:id} : delete the "id" boxitem.
     *
     * @param id the id of the boxitem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boxitems/{id}")
    public ResponseEntity<Void> deleteBoxitem(@PathVariable Long id) {
        log.debug("REST request to delete Boxitem : {}", id);
        boxitemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
