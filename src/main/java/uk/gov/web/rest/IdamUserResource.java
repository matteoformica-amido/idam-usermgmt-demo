package uk.gov.web.rest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.domain.IdamUser;
import uk.gov.domain.IdmUsers;
import uk.gov.hmcts.reform.idam.api.external.UserManagementApi;
import uk.gov.hmcts.reform.idam.api.shared.model.User;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link uk.gov.domain.IdamUser}.
 */
@RestController
@RequestMapping("/api")
public class IdamUserResource {

    private final Logger log = LoggerFactory.getLogger(IdamUserResource.class);

    private static final String ENTITY_NAME = "idamUser";

    private static final String IDAM_API_USERS_BASE_URI = "http://localhost:5000/api/v1/users";
    private static final String FR_IDM_API_GET_ALL_USERS_URI = "http://localhost:18080/openidm/managed/user?_pageSize=100&_queryFilter=true&_fields=mail,userName,givenName,sn,accountStatus,roles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdamUserService idamUserService;

    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserManagementApi userManagementApi;

    @Autowired
    private UserManagementApi userRoleManagementApi;

    @Autowired
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

    private String getAccessToken(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient client =
            clientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        String tokenType = client.getAccessToken().getTokenType().getValue();
        System.out.println("TOKEN: "+client.getAccessToken().getTokenValue());
        return client.getAccessToken().getTokenValue();
    }

    /**
     * {@code GET  /idam-users} : get all the idamUsers.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of idamUsers in body.
     */
    @GetMapping("/idam-users")
    public ResponseEntity<List<IdamUser>> getAllIdamUsers(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        //headers2.setBearerAuth(getAccessToken());
        headers2.add("X-OpenIDM-Username", "openidm-admin");
        headers2.add("X-OpenIDM-Password", "openidm-admin");

        ResponseEntity<IdmUsers> entity = restTemplate.exchange(
            FR_IDM_API_GET_ALL_USERS_URI, HttpMethod.GET, new HttpEntity<IdmUsers>(headers2),
            IdmUsers.class);

        //User u = userManagementApi.getUserByEmail("Bearer "+token, "demouser@hmcts.net");
        System.out.println("IDM USERS: "+entity.getBody().users.size());

        log.debug("REST request to get a page of IdamUsers");

        

        Page<IdamUser> page;
        if (eagerload) {
            page = idamUserService.findAllWithEagerRelationships(pageable);
        } else {
            page = idamUserService.findAll(pageable);
        }
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
