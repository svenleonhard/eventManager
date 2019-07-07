package de.sevenldev.eventmanager.gateway.repository.search;

import de.sevenldev.eventmanager.gateway.domain.BoxItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BoxItem} entity.
 */
public interface BoxItemSearchRepository extends ElasticsearchRepository<BoxItem, Long> {
}
