package uk.gov.web.rest;

import com.google.common.collect.Lists;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.domain.IdamUser;
import uk.gov.domain.IdmUsers;
import uk.gov.hmcts.reform.idam.api.shared.model.User;
import uk.gov.repository.search.IdamUserSearchRepository;
import uk.gov.service.IdamUserService;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link uk.gov.domain.IdamUser}.
 */
@RestController
@RequestMapping("/api")
public class IdamUserResource extends BaseResource{

    private final Logger log = LoggerFactory.getLogger(IdamUserResource.class);

    private static final String ENTITY_NAME = "idamUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdamUserService idamUserService;

    @Autowired
    protected IdamUserSearchRepository idamUserSearchRepository;

    @Autowired
    public IdamUserResource(IdamUserService idamUserService) {
        this.idamUserService = idamUserService;
    }

    @PostConstruct
    public void populateIndex(){
        loadAllIdmUsers();
    }

    private void loadAllIdmUsers(){
        ResponseEntity<IdmUsers> users = getIdmUsers();
        Page<IdamUser> page = mapIdmUsers(users.getBody().getUsers());
        idamUserSearchRepository.deleteAll();
        idamUserSearchRepository.saveAll(page.getContent());
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
//        if (idamUser.getId() != null) {
//            throw new BadRequestAlertException("A new idamUser cannot already have an ID", ENTITY_NAME, "idexists");
//        }
        List<String> roles;
        if(idamUser.getRolesList()!=null) {
            roles = Arrays.asList(idamUser.getRolesList().split(","));
        }else{
            roles = Collections.emptyList();
        }
        System.out.println("USER roles: "+idamUser.getRolesList());

        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);

        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("UPDATING USER: "+idamUser.getUid());

        User mappedUser = mapIdamUserToUser(idamUser);
        mappedUser.setRoles(roles);
        mappedUser.setPending(null);
        mappedUser.setActive(null);
        HttpEntity<User> requestUpdate = new HttpEntity<>(mappedUser, headers);
        ResponseEntity<User> entity = restTemplate.exchange(
            IDAM_API_REG_USER_URI, HttpMethod.POST, requestUpdate,
            User.class);

        //IdamUser result = mapUserToIdamUser(entity.getBody());
        if(idamUser.getUid()!=null){
            ResponseEntity<User> pendingUser = restTemplate.exchange(
                IDAM_API_USERS_BASE_URI+idamUser.getUid(), HttpMethod.GET, new HttpEntity<User>(headers),
                User.class);

            IdamUser result = mapUserToIdamUser(pendingUser.getBody());
            System.out.println("RETRIEVED PENDING: "+pendingUser.getBody());
            System.out.println("MAPPED: "+result);
            idamUserSearchRepository.save(result);
        }

//        IdamUser result = idamUserService.save(idamUser);
        return ResponseEntity.created(new URI("/api/idam-users/"+System.currentTimeMillis()))
            //.headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(null);
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

        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);

        restTemplate.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println("UPDATING USER: "+idamUser.getUid());

        User mappedUser = mapIdamUserToUser(idamUser);
        mappedUser.setRoles(null);
        HttpEntity<User> requestUpdate = new HttpEntity<>(mappedUser, headers);
        ResponseEntity<User> entity = restTemplate.exchange(
            IDAM_API_USERS_BASE_URI+idamUser.getUid(), HttpMethod.PATCH, requestUpdate,
            User.class);

        IdamUser result = mapUserToIdamUser(entity.getBody());
        idamUserSearchRepository.save(result);
        loadAllIdmUsers();
        //idamUserSearchRepository.refresh();
        //IdamUser result = idamUserService.save(idamUser);
        return ResponseEntity.ok()
            //.headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
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

        //ResponseEntity<IdmUsers> users = getIdmUsers();
        idamUserSearchRepository.refresh();
        Page<IdamUser> page = new PageImpl<>(Lists.newArrayList(idamUserSearchRepository.findAll()));
        //idamUserSearchRepository.saveAll(page.getContent());
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

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        System.out.println("FETCHING USER: "+userUids.get(id));

        ResponseEntity<User> entity = restTemplate.exchange(
            IDAM_API_USERS_BASE_URI+userUids.get(id), HttpMethod.GET, new HttpEntity<User>(headers),
            User.class);

        IdamUser user = mapUserToIdamUser(entity.getBody());

        return ResponseUtil.wrapOrNotFound(Optional.of(user));
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
