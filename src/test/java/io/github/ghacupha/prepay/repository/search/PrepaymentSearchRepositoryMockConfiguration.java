package io.github.ghacupha.prepay.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PrepaymentSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PrepaymentSearchRepositoryMockConfiguration {

    @MockBean
    private PrepaymentSearchRepository mockPrepaymentSearchRepository;

}
