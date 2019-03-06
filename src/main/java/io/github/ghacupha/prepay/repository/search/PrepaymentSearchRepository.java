package io.github.ghacupha.prepay.repository.search;

import io.github.ghacupha.prepay.domain.Prepayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Prepayment entity.
 */
public interface PrepaymentSearchRepository extends ElasticsearchRepository<Prepayment, Long> {
}
