package de.sevenldev.eventmanager.box.repository;

import de.sevenldev.eventmanager.box.domain.Boxitem;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Boxitem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoxitemRepository extends MongoRepository<Boxitem, String> {

}
