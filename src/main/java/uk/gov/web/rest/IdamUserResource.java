package uk.gov.web.rest;

import uk.gov.domain.IdamUser;
import uk.gov.service.IdamUserService;
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
 * REST controller for managing {@link uk.gov.domain.IdamUser}.
 */
@RestController
@RequestMapping("/api")
public class IdamUserResource {

    private final Logger log = LoggerFactory.getLogger(IdamUserResource.class);

    private static final String ENTITY_NAME = "idamUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdamUserService idamUserService;

    public IdamUserResource(IdamUserService idamUserService) {
        this.idamUserService = idamUserService;
    }

    /**
     * {@code POST  /idam-users} : Create a new idamUser.
     *
     * @param idamUser the idamUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new idamUser, or with status {@code 400 (Bad Request)} if the idamUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/idam-users")
    public ResponseEntity<IdamUser> createIdamUser(@RequestBody IdamUser idamUser) throws URISyntaxException {
        log.debug("REST request to save IdamUser : {}", idamUser);
        if (idamUser.getId() != null) {
            throw new BadRequestAlertException("A new idamUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IdamUser result = idamUserService.save(idamUser);
        return ResponseEntity.created(new URI("/api/idam-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /idam-users} : Updates an existing idamUser.
     *
     * @param idamUser the idamUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated idamUser,
     * or with status {@code 400 (Bad Request)} if the idamUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the idamUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/idam-users")
    public ResponseEntity<IdamUser> updateIdamUser(@RequestBody IdamUser idamUser) throws URISyntaxException {
        log.debug("REST request to update IdamUser : {}", idamUser);
        if (idamUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IdamUser result = idamUserService.save(idamUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, idamUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /idam-users} : get all the idamUsers.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of idamUsers in body.
     */
    @GetMapping("/idam-users")
    public ResponseEntity<List<IdamUser>> getAllIdamUsers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of IdamUsers");
        Page<IdamUser> page = idamUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /idam-users/:id} : get the "id" idamUser.
     *
     * @param id the id of the idamUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the idamUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/idam-users/{id}")
    public ResponseEntity<IdamUser> getIdamUser(@PathVariable Long id) {
        log.debug("REST request to get IdamUser : {}", id);
        Optional<IdamUser> idamUser = idamUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(idamUser);
    }

    /**
     * {@code DELETE  /idam-users/:id} : delete the "id" idamUser.
     *
     * @param id the id of the idamUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/idam-users/{id}")
    public ResponseEntity<Void> deleteIdamUser(@PathVariable Long id) {
        log.debug("REST request to delete IdamUser : {}", id);
        idamUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/idam-users?query=:query} : search for the idamUser corresponding
     * to the query.
     *
     * @param query the query of the idamUser search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/idam-users")
    public ResponseEntity<List<IdamUser>> searchIdamUsers(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of IdamUsers for query {}", query);
        Page<IdamUser> page = idamUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
