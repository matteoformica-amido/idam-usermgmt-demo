package uk.gov.service;

import uk.gov.domain.IdamUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link IdamUser}.
 */
public interface IdamUserService {

    /**
     * Save a idamUser.
     *
     * @param idamUser the entity to save.
     * @return the persisted entity.
     */
    IdamUser save(IdamUser idamUser);

    /**
     * Get all the idamUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdamUser> findAll(Pageable pageable);


    /**
     * Get the "id" idamUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IdamUser> findOne(Long id);

    /**
     * Delete the "id" idamUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the idamUser corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IdamUser> search(String query, Pageable pageable);
}
