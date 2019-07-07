package de.sevenldev.eventmanager.box.repository.search;

import de.sevenldev.eventmanager.box.domain.Box;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Box} entity.
 */
public interface BoxSearchRepository extends ElasticsearchRepository<Box, Long> {
}
