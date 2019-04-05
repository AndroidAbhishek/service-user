package com.welzuka.user.user.repository.search;

import com.welzuka.user.user.domain.DoctorAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DoctorAccount entity.
 */
public interface DoctorAccountSearchRepository extends ElasticsearchRepository<DoctorAccount, Long> {
}
