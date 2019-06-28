package uk.gov.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;
import uk.gov.domain.IdamRole;
import uk.gov.domain.IdamUser;
import uk.gov.domain.IdmRole;
import uk.gov.domain.IdmRoles;
import uk.gov.domain.IdmUser;
import uk.gov.domain.IdmUserRole;
import uk.gov.domain.IdmUsers;
import uk.gov.hmcts.reform.idam.api.external.UserManagementApi;
import uk.gov.hmcts.reform.idam.api.shared.model.User;
import uk.gov.repository.search.IdamRoleSearchRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BaseResource {

    protected static final String IDAM_API_USERS_BASE_URI = "http://localhost:5000/api/v1/users/";
    protected static final String IDAM_API_REG_USER_URI = "http://localhost:5000/api/v1/users/registration";
    protected static final String FR_IDM_API_GET_ALL_USERS_URI = "http://localhost:18080/openidm/managed/user?_pageSize=100&_queryFilter=true&_fields=mail,userName,givenName,sn,accountStatus,roles";
    protected static final String FR_IDM_API_GET_ALL_ROLES_URI = "http://localhost:18080/openidm/managed/role?_pageSize=100&_queryFilter=true&_fields=_id,name";
    protected static final String FR_IDM_API_GET_ROLE_BY_ID_BASE_URI = "http://localhost:18080/openidm/managed/role/";

    protected Map<String, String> roleNames = new HashMap<>();
    protected Map<Long, String> userUids = new HashMap();

    @Autowired
    protected OAuth2AuthorizedClientService clientService;

    @Autowired
    protected UserManagementApi userManagementApi;

    @Autowired
    protected UserManagementApi userRoleManagementApi;

    protected String getAccessToken(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient client =
            clientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        //String tokenType = client.getAccessToken().getTokenType().getValue();
        //System.out.println("TOKEN: "+client.getAccessToken().getTokenValue());
        return client.getAccessToken().getTokenValue();
    }

    protected ResponseEntity<IdmUsers> getIdmUsers(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("X-OpenIDM-Username", "openidm-admin");
        headers2.add("X-OpenIDM-Password", "openidm-admin");

        ResponseEntity<IdmUsers> entity = restTemplate.exchange(
            FR_IDM_API_GET_ALL_USERS_URI, HttpMethod.GET, new HttpEntity<IdmUsers>(headers2),
            IdmUsers.class);

        return entity;
    }

    protected IdamRole getIdmRoleById(String id){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-OpenIDM-Username", "openidm-admin");
        headers.add("X-OpenIDM-Password", "openidm-admin");

        ResponseEntity<IdmRole> entity = restTemplate.exchange(
            FR_IDM_API_GET_ROLE_BY_ID_BASE_URI+id, HttpMethod.GET, new HttpEntity<IdmRole>(headers),
            IdmRole.class);

        roleNames.put(entity.getBody().get_id(), entity.getBody().getName());

        Set<IdamRole> roles = mapIdmRoles(Collections.singletonList(entity.getBody()));
        return new ArrayList<>(roles).get(0);
    }

    protected Set<IdamRole> getIdmRoles(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("X-OpenIDM-Username", "openidm-admin");
        headers2.add("X-OpenIDM-Password", "openidm-admin");

        ResponseEntity<IdmRoles> entity = restTemplate.exchange(
            FR_IDM_API_GET_ALL_ROLES_URI, HttpMethod.GET, new HttpEntity<IdmRoles>(headers2),
            IdmRoles.class);

        for(IdmRole role : entity.getBody().getRoles()){
            roleNames.put(role.get_id(), role.getName());
        }
        return mapIdmRoles(entity.getBody().getRoles());
    }

    protected Page<IdamUser> mapIdmUsers(List<IdmUser> idmUsers){
        List<IdamUser> convertedUsers = new ArrayList<>();
        Long i = 0L;
        for(IdmUser user : idmUsers){
            IdamUser u = new IdamUser();
            u.setEmail(user.getEmail());
            u.setFirstName(user.getForename());
            u.setId(i++);
            u.setLastName(user.getSurname());
            u.setUid(UUID.fromString(user.get_id()));
            u.setStatus(user.getAccountStatus());
            u.setRoles(mapIdmUserRoles(user.getRoles()));
            convertedUsers.add(u);
            userUids.put(u.getId(),u.getUid().toString());
        }
        return new PageImpl<>(convertedUsers);
    }

    protected Set<IdamRole> mapIdmUserRoles(List<IdmUserRole> idmRoles){
        Long i = 0L;
        Set<IdamRole> roles = new HashSet<>();
        for(IdmUserRole idmRole : idmRoles){
            IdamRole r = new IdamRole();
            r.setId(i++);
            r.setRoleName((String)roleNames.get(idmRole.get_ref().split("/")[2]));
            roles.add(r);
        }
        return roles;
    }

    protected Set<IdamRole> mapIdmRoles(List<IdmRole> idmRoles){
        Long i = 0L;
        Set<IdamRole> roles = new HashSet<>();
        for(IdmRole idmRole : idmRoles){
            IdamRole r = new IdamRole();
            r.setId(i++);
            r.setRoleName((String)roleNames.get(idmRole.get_id()));
            roles.add(r);
        }
        return roles;
    }

    protected IdamUser mapUserToIdamUser(User u){
        IdamUser idamUser = new IdamUser();
        idamUser.setRoles(mapRolesToIdamRoles(u.getRoles()));
        if(u.isActive() != null ) {
            idamUser.setStatus(u.isActive() ? "active" : "inactive");
        }
        if(u.isPending()!=null){
            idamUser.setStatus(u.isPending() ? "pending" : "N/A");
        }
        idamUser.setUid(UUID.fromString(u.getId()));
        idamUser.setLastName(u.getSurname());
        idamUser.setFirstName(u.getForename());
        idamUser.setEmail(u.getEmail());
        return idamUser;
    }

    protected User mapIdamUserToUser(IdamUser u){
        User user = new User();
        user.setRoles(new ArrayList<>(mapIdamRolesToRoles(u.getRoles())));
        if(u.getStatus()!=null) {
            user.setActive(u.getStatus().equals("active") ? true : false);
            user.setPending(u.getStatus().equals("pending") ? true : false);
        }
        if(u.getUid()!=null) {
            user.setId(u.getUid().toString());
        }
        user.setSurname(u.getLastName());
        user.setForename(u.getFirstName());
        user.setEmail(u.getEmail());
        return user;
    }

    protected Set<IdamRole> mapRolesToIdamRoles(List<String> roleIds){
        Set<IdamRole> idamRoles = new HashSet<>();
        Long i = 0L;
        if(roleIds!=null) {
            for (String roleId : roleIds) {
                IdamRole idamRole = new IdamRole();
                idamRole.setRoleName(getIdmRoleById(roleId).getRoleName());
                idamRole.setId(i++);
                idamRoles.add(idamRole);
            }
        }
        return idamRoles;
    }

    protected Set<String> mapIdamRolesToRoles(Set<IdamRole> inputRoles){
        Set<String> roles = new HashSet<>();
        for(IdamRole inputRole : inputRoles) {
            roles.add(inputRole.getRoleName());
        }
        return roles;
    }

}
