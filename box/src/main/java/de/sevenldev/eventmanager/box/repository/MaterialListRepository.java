package de.sevenldev.eventmanager.box.repository;

import de.sevenldev.eventmanager.box.domain.MaterialList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MaterialList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialListRepository extends JpaRepository<MaterialList, Long> {

}
