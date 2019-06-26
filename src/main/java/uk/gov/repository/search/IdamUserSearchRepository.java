package uk.gov.repository.search;

import uk.gov.domain.IdamUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IdamUser} entity.
 */
public interface IdamUserSearchRepository extends ElasticsearchRepository<IdamUser, Long> {
}
