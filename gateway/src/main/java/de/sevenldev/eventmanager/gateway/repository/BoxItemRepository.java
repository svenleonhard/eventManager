package de.sevenldev.eventmanager.gateway.repository;

import de.sevenldev.eventmanager.gateway.domain.BoxItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BoxItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxItemRepository extends JpaRepository<BoxItem, Long> {

}
