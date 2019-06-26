package uk.gov.repository.search;

import uk.gov.domain.IdamRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IdamRole} entity.
 */
public interface IdamRoleSearchRepository extends ElasticsearchRepository<IdamRole, Long> {
}
