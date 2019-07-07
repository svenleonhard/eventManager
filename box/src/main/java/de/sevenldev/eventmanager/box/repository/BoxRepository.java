package de.sevenldev.eventmanager.box.repository;

import de.sevenldev.eventmanager.box.domain.Box;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Box entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxRepository extends MongoRepository<Box, String> {

}
