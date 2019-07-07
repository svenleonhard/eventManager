package de.sevenldev.eventmanager.box.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link BoxSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BoxSearchRepositoryMockConfiguration {

    @MockBean
    private BoxSearchRepository mockBoxSearchRepository;

}
