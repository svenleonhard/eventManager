package de.sevenldev.eventmanager.box.service.impl;

import de.sevenldev.eventmanager.box.service.BoxitemService;
import de.sevenldev.eventmanager.box.domain.Boxitem;
import de.sevenldev.eventmanager.box.repository.BoxitemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Boxitem}.
 */
@Service
@Transactional
public class BoxitemServiceImpl implements BoxitemService {

    private final Logger log = LoggerFactory.getLogger(BoxitemServiceImpl.class);

    private final BoxitemRepository boxitemRepository;

    public BoxitemServiceImpl(BoxitemRepository boxitemRepository) {
        this.boxitemRepository = boxitemRepository;
    }

    /**
     * Save a boxitem.
     *
     * @param boxitem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Boxitem save(Boxitem boxitem) {
        log.debug("Request to save Boxitem : {}", boxitem);
        return boxitemRepository.save(boxitem);
    }

    /**
     * Get all the boxitems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Boxitem> findAll(Pageable pageable) {
        log.debug("Request to get all Boxitems");
        return boxitemRepository.findAll(pageable);
    }


    /**
     * Get one boxitem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Boxitem> findOne(Long id) {
        log.debug("Request to get Boxitem : {}", id);
        return boxitemRepository.findById(id);
    }

    /**
     * Delete the boxitem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Boxitem : {}", id);
        boxitemRepository.deleteById(id);
    }
}
