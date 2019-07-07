package de.sevenldev.eventmanager.gateway.repository.search;

import de.sevenldev.eventmanager.gateway.domain.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Item} entity.
 */
public interface ItemSearchRepository extends ElasticsearchRepository<Item, Long> {
}
