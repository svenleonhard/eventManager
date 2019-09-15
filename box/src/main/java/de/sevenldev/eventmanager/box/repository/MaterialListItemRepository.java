package de.sevenldev.eventmanager.box.repository;

import de.sevenldev.eventmanager.box.domain.MaterialListItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MaterialListItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialListItemRepository extends JpaRepository<MaterialListItem, Long> {

}
