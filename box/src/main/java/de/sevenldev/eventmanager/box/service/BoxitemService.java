package de.sevenldev.eventmanager.box.service;

import de.sevenldev.eventmanager.box.domain.Boxitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Boxitem}.
 */
public interface BoxitemService {

    /**
     * Save a boxitem.
     *
     * @param boxitem the entity to save.
     * @return the persisted entity.
     */
    Boxitem save(Boxitem boxitem);

    /**
     * Get all the boxitems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Boxitem> findAll(Pageable pageable);


    /**
     * Get the "id" boxitem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Boxitem> findOne(Long id);

    /**
     * Delete the "id" boxitem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
