package com.welzuka.user.user.repository.search;

import com.welzuka.user.user.domain.GeneralAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GeneralAccount entity.
 */
public interface GeneralAccountSearchRepository extends ElasticsearchRepository<GeneralAccount, Long> {
}
