package uk.gov.service;

import uk.gov.domain.IdamRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link IdamRole}.
 */
public interface IdamRoleService {

    /**
     * Save a idamRole.
     *
     * @param idamRole the entity to save.
     * @return the persisted entity.
     */
    IdamRole save(IdamRole idamRole);

    /**
     * Get all the idamRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdamRole> findAll(Pageable pageable);

    /**
     * Get all the idamRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<IdamRole> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" idamRole.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdamRole> findOne(Long id);

    /**
     * Delete the "id" idamRole.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the idamRole corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdamRole> search(String query, Pageable pageable);
}
