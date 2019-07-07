package de.sevenldev.eventmanager.gateway.web.rest;

import de.sevenldev.eventmanager.gateway.domain.BoxItem;
import de.sevenldev.eventmanager.gateway.repository.BoxItemRepository;
import de.sevenldev.eventmanager.gateway.repository.search.BoxItemSearchRepository;
import de.sevenldev.eventmanager.gateway.web.rest.errors.BadRequestAlertException;

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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link de.sevenldev.eventmanager.gateway.domain.BoxItem}.
 */
@RestController
@RequestMapping("/api")
public class BoxItemResource {

    private final Logger log = LoggerFactory.getLogger(BoxItemResource.class);

    private static final String ENTITY_NAME = "boxItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoxItemRepository boxItemRepository;

    private final BoxItemSearchRepository boxItemSearchRepository;

    public BoxItemResource(BoxItemRepository boxItemRepository, BoxItemSearchRepository boxItemSearchRepository) {
        this.boxItemRepository = boxItemRepository;
        this.boxItemSearchRepository = boxItemSearchRepository;
    }

    /**
     * {@code POST  /box-items} : Create a new boxItem.
     *
     * @param boxItem the boxItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boxItem, or with status {@code 400 (Bad Request)} if the boxItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/box-items")
    public ResponseEntity<BoxItem> createBoxItem(@RequestBody BoxItem boxItem) throws URISyntaxException {
        log.debug("REST request to save BoxItem : {}", boxItem);
        if (boxItem.getId() != null) {
            throw new BadRequestAlertException("A new boxItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoxItem result = boxItemRepository.save(boxItem);
        boxItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/box-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /box-items} : Updates an existing boxItem.
     *
     * @param boxItem the boxItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boxItem,
     * or with status {@code 400 (Bad Request)} if the boxItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boxItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/box-items")
    public ResponseEntity<BoxItem> updateBoxItem(@RequestBody BoxItem boxItem) throws URISyntaxException {
        log.debug("REST request to update BoxItem : {}", boxItem);
        if (boxItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoxItem result = boxItemRepository.save(boxItem);
        boxItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boxItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /box-items} : get all the boxItems.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boxItems in body.
     */
    @GetMapping("/box-items")
    public ResponseEntity<List<BoxItem>> getAllBoxItems(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of BoxItems");
        Page<BoxItem> page = boxItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /box-items/:id} : get the "id" boxItem.
     *
     * @param id the id of the boxItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boxItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/box-items/{id}")
    public ResponseEntity<BoxItem> getBoxItem(@PathVariable Long id) {
        log.debug("REST request to get BoxItem : {}", id);
        Optional<BoxItem> boxItem = boxItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boxItem);
    }

    /**
     * {@code DELETE  /box-items/:id} : delete the "id" boxItem.
     *
     * @param id the id of the boxItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/box-items/{id}")
    public ResponseEntity<Void> deleteBoxItem(@PathVariable Long id) {
        log.debug("REST request to delete BoxItem : {}", id);
        boxItemRepository.deleteById(id);
        boxItemSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/box-items?query=:query} : search for the boxItem corresponding
     * to the query.
     *
     * @param query the query of the boxItem search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/box-items")
    public ResponseEntity<List<BoxItem>> searchBoxItems(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of BoxItems for query {}", query);
        Page<BoxItem> page = boxItemSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
