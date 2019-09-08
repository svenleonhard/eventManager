package de.sevenldev.eventmanager.box.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import de.sevenldev.eventmanager.box.domain.Boxitem;
import de.sevenldev.eventmanager.box.domain.*; // for static metamodels
import de.sevenldev.eventmanager.box.repository.BoxitemRepository;
import de.sevenldev.eventmanager.box.service.dto.BoxitemCriteria;

/**
 * Service for executing complex queries for {@link Boxitem} entities in the database.
 * The main input is a {@link BoxitemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Boxitem} or a {@link Page} of {@link Boxitem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoxitemQueryService extends QueryService<Boxitem> {

    private final Logger log = LoggerFactory.getLogger(BoxitemQueryService.class);

    private final BoxitemRepository boxitemRepository;

    public BoxitemQueryService(BoxitemRepository boxitemRepository) {
        this.boxitemRepository = boxitemRepository;
    }

    /**
     * Return a {@link List} of {@link Boxitem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Boxitem> findByCriteria(BoxitemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Boxitem> specification = createSpecification(criteria);
        return boxitemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Boxitem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Boxitem> findByCriteria(BoxitemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Boxitem> specification = createSpecification(criteria);
        return boxitemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoxitemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Boxitem> specification = createSpecification(criteria);
        return boxitemRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Boxitem> createSpecification(BoxitemCriteria criteria) {
        Specification<Boxitem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Boxitem_.id));
            }
            if (criteria.getToRepair() != null) {
                specification = specification.and(buildSpecification(criteria.getToRepair(), Boxitem_.toRepair));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), Boxitem_.comment));
            }
            if (criteria.getBoxId() != null) {
                specification = specification.and(buildSpecification(criteria.getBoxId(),
                    root -> root.join(Boxitem_.box, JoinType.LEFT).get(Box_.id)));
            }
            if (criteria.getItemId() != null) {
                specification = specification.and(buildSpecification(criteria.getItemId(),
                    root -> root.join(Boxitem_.item, JoinType.LEFT).get(Item_.id)));
            }
        }
        return specification;
    }
}
