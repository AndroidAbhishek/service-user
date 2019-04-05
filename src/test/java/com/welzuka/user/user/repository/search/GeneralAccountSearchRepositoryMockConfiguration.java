package com.welzuka.user.user.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of GeneralAccountSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GeneralAccountSearchRepositoryMockConfiguration {

    @MockBean
    private GeneralAccountSearchRepository mockGeneralAccountSearchRepository;

}
