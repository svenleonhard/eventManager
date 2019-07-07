package de.sevenldev.eventmanager.gateway.repository;

import de.sevenldev.eventmanager.gateway.domain.Box;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Box entity.
 */
@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    @Query(value = "select distinct box from Box box left join fetch box.users",
        countQuery = "select count(distinct box) from Box box")
    Page<Box> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct box from Box box left join fetch box.users")
    List<Box> findAllWithEagerRelationships();

    @Query("select box from Box box left join fetch box.users where box.id =:id")
    Optional<Box> findOneWithEagerRelationships(@Param("id") Long id);

}
