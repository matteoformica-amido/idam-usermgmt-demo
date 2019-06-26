package uk.gov.repository;

import uk.gov.domain.IdamUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the IdamUser entity.
 */
@Repository
public interface IdamUserRepository extends JpaRepository<IdamUser, Long> {

    @Query(value = "select distinct idamUser from IdamUser idamUser left join fetch idamUser.roles",
        countQuery = "select count(distinct idamUser) from IdamUser idamUser")
    Page<IdamUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct idamUser from IdamUser idamUser left join fetch idamUser.roles")
    List<IdamUser> findAllWithEagerRelationships();

    @Query("select idamUser from IdamUser idamUser left join fetch idamUser.roles where idamUser.id =:id")
    Optional<IdamUser> findOneWithEagerRelationships(@Param("id") Long id);

}
