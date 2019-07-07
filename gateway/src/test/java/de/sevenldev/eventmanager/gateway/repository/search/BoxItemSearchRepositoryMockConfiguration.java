package de.sevenldev.eventmanager.gateway.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link BoxItemSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BoxItemSearchRepositoryMockConfiguration {

    @MockBean
    private BoxItemSearchRepository mockBoxItemSearchRepository;

}
