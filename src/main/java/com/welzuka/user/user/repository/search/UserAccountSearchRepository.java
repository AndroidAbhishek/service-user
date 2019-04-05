package com.welzuka.user.user.repository.search;

import com.welzuka.user.user.domain.UserAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserAccount entity.
 */
public interface UserAccountSearchRepository extends ElasticsearchRepository<UserAccount, Long> {
}
