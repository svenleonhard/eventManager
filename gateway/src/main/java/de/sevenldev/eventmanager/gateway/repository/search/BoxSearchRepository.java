package de.sevenldev.eventmanager.gateway.repository.search;

import de.sevenldev.eventmanager.gateway.domain.Box;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Box} entity.
 */
public interface BoxSearchRepository extends ElasticsearchRepository<Box, Long> {
}
