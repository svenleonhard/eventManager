package de.sevenldev.eventmanager.box.repository;

import de.sevenldev.eventmanager.box.domain.Boxitem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Boxitem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxitemRepository extends JpaRepository<Boxitem, Long> {

}
