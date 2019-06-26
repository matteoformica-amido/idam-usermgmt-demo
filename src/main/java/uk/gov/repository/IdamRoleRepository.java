package uk.gov.repository;

import uk.gov.domain.IdamRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the IdamRole entity.
 */
@Repository
public interface IdamRoleRepository extends JpaRepository<IdamRole, Long> {

    @Query(value = "select distinct idamRole from IdamRole idamRole left join fetch idamRole.members",
        countQuery = "select count(distinct idamRole) from IdamRole idamRole")
    Page<IdamRole> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct idamRole from IdamRole idamRole left join fetch idamRole.members")
    List<IdamRole> findAllWithEagerRelationships();

    @Query("select idamRole from IdamRole idamRole left join fetch idamRole.members where idamRole.id =:id")
    Optional<IdamRole> findOneWithEagerRelationships(@Param("id") Long id);

}
