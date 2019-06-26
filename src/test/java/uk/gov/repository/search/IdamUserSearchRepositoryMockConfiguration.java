package uk.gov.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link IdamUserSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IdamUserSearchRepositoryMockConfiguration {

    @MockBean
    private IdamUserSearchRepository mockIdamUserSearchRepository;

}
