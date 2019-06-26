package uk.gov.repository;

import uk.gov.domain.IdamUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IdamUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdamUserRepository extends JpaRepository<IdamUser, Long> {

}
