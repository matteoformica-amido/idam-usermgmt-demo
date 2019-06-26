package uk.gov.service.impl;

import uk.gov.service.IdamRoleService;
import uk.gov.domain.IdamRole;
import uk.gov.repository.IdamRoleRepository;
import uk.gov.repository.search.IdamRoleSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link IdamRole}.
 */
@Service
@Transactional
public class IdamRoleServiceImpl implements IdamRoleService {

    private final Logger log = LoggerFactory.getLogger(IdamRoleServiceImpl.class);

    private final IdamRoleRepository idamRoleRepository;

    private final IdamRoleSearchRepository idamRoleSearchRepository;

    public IdamRoleServiceImpl(IdamRoleRepository idamRoleRepository, IdamRoleSearchRepository idamRoleSearchRepository) {
        this.idamRoleRepository = idamRoleRepository;
        this.idamRoleSearchRepository = idamRoleSearchRepository;
    }

    /**
     * Save a idamRole.
     *
     * @param idamRole the entity to save.
     * @return the persisted entity.
     */
    @Override
    public IdamRole save(IdamRole idamRole) {
        log.debug("Request to save IdamRole : {}", idamRole);
        IdamRole result = idamRoleRepository.save(idamRole);
        idamRoleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the idamRoles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IdamRole> findAll(Pageable pageable) {
        log.debug("Request to get all IdamRoles");
        return idamRoleRepository.findAll(pageable);
    }

    /**
     * Get all the idamRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IdamRole> findAllWithEagerRelationships(Pageable pageable) {
        return idamRoleRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one idamRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IdamRole> findOne(Long id) {
        log.debug("Request to get IdamRole : {}", id);
        return idamRoleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the idamRole by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IdamRole : {}", id);
        idamRoleRepository.deleteById(id);
        idamRoleSearchRepository.deleteById(id);
    }

    /**
     * Search for the idamRole corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IdamRole> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IdamRoles for query {}", query);
        return idamRoleSearchRepository.search(queryStringQuery(query), pageable);    }
}
