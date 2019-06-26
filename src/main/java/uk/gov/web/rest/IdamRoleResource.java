package uk.gov.web.rest;

import uk.gov.domain.IdamRole;
import uk.gov.service.IdamRoleService;
import uk.gov.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link uk.gov.domain.IdamRole}.
 */
@RestController
@RequestMapping("/api")
public class IdamRoleResource {

    private final Logger log = LoggerFactory.getLogger(IdamRoleResource.class);

    private static final String ENTITY_NAME = "idamRole";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdamRoleService idamRoleService;

    public IdamRoleResource(IdamRoleService idamRoleService) {
        this.idamRoleService = idamRoleService;
    }

    /**
     * {@code POST  /idam-roles} : Create a new idamRole.
     *
     * @param idamRole the idamRole to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idamRole, or with status {@code 400 (Bad Request)} if the idamRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/idam-roles")
    public ResponseEntity<IdamRole> createIdamRole(@RequestBody IdamRole idamRole) throws URISyntaxException {
        log.debug("REST request to save IdamRole : {}", idamRole);
        if (idamRole.getId() != null) {
            throw new BadRequestAlertException("A new idamRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdamRole result = idamRoleService.save(idamRole);
        return ResponseEntity.created(new URI("/api/idam-roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /idam-roles} : Updates an existing idamRole.
     *
     * @param idamRole the idamRole to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idamRole,
     * or with status {@code 400 (Bad Request)} if the idamRole is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idamRole couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/idam-roles")
    public ResponseEntity<IdamRole> updateIdamRole(@RequestBody IdamRole idamRole) throws URISyntaxException {
        log.debug("REST request to update IdamRole : {}", idamRole);
        if (idamRole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IdamRole result = idamRoleService.save(idamRole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, idamRole.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /idam-roles} : get all the idamRoles.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of idamRoles in body.
     */
    @GetMapping("/idam-roles")
    public ResponseEntity<List<IdamRole>> getAllIdamRoles(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of IdamRoles");
        Page<IdamRole> page;
        if (eagerload) {
            page = idamRoleService.findAllWithEagerRelationships(pageable);
        } else {
            page = idamRoleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idam-roles/:id} : get the "id" idamRole.
     *
     * @param id the id of the idamRole to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idamRole, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/idam-roles/{id}")
    public ResponseEntity<IdamRole> getIdamRole(@PathVariable Long id) {
        log.debug("REST request to get IdamRole : {}", id);
        Optional<IdamRole> idamRole = idamRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(idamRole);
    }

    /**
     * {@code DELETE  /idam-roles/:id} : delete the "id" idamRole.
     *
     * @param id the id of the idamRole to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/idam-roles/{id}")
    public ResponseEntity<Void> deleteIdamRole(@PathVariable Long id) {
        log.debug("REST request to delete IdamRole : {}", id);
        idamRoleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/idam-roles?query=:query} : search for the idamRole corresponding
     * to the query.
     *
     * @param query the query of the idamRole search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/idam-roles")
    public ResponseEntity<List<IdamRole>> searchIdamRoles(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of IdamRoles for query {}", query);
        Page<IdamRole> page = idamRoleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
