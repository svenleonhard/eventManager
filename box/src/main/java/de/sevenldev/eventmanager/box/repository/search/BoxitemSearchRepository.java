package de.sevenldev.eventmanager.box.repository.search;

import de.sevenldev.eventmanager.box.domain.Boxitem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Boxitem} entity.
 */
public interface BoxitemSearchRepository extends ElasticsearchRepository<Boxitem, Long> {
}
