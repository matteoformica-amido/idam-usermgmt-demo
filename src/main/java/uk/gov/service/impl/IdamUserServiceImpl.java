package uk.gov.service.impl;

import uk.gov.service.IdamUserService;
import uk.gov.domain.IdamUser;
import uk.gov.repository.IdamUserRepository;
import uk.gov.repository.search.IdamUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link IdamUser}.
 */
@Service
@Transactional
public class IdamUserServiceImpl implements IdamUserService {

    private final Logger log = LoggerFactory.getLogger(IdamUserServiceImpl.class);

    private final IdamUserRepository idamUserRepository;

    private final IdamUserSearchRepository idamUserSearchRepository;

    public IdamUserServiceImpl(IdamUserRepository idamUserRepository, IdamUserSearchRepository idamUserSearchRepository) {
        this.idamUserRepository = idamUserRepository;
        this.idamUserSearchRepository = idamUserSearchRepository;
    }

    /**
     * Save a idamUser.
     *
     * @param idamUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public IdamUser save(IdamUser idamUser) {
        log.debug("Request to save IdamUser : {}", idamUser);
        IdamUser result = idamUserRepository.save(idamUser);
        idamUserSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the idamUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IdamUser> findAll(Pageable pageable) {
        log.debug("Request to get all IdamUsers");
        return idamUserRepository.findAll(pageable);
    }


    /**
     * Get one idamUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IdamUser> findOne(Long id) {
        log.debug("Request to get IdamUser : {}", id);
        return idamUserRepository.findById(id);
    }

    /**
     * Delete the idamUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IdamUser : {}", id);
        idamUserRepository.deleteById(id);
        idamUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the idamUser corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IdamUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IdamUsers for query {}", query);
        return idamUserSearchRepository.search(queryStringQuery(query), pageable);    }
}
