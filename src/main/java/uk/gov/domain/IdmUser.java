package uk.gov.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by efthymios.kartsonakis on 02/08/2017.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdmUser {

    @JsonProperty
    private String _id;

    @JsonProperty
    private String _rev;

    @JsonProperty
    private String userName;

    @JsonProperty("givenName")
    private String forename;

    @JsonProperty("sn")
    private String surname;

    @JsonProperty("mail")
    private String email;

    @JsonProperty
    private String accountStatus;

    @JsonProperty
    private List<IdmUserRole> roles;

    @JsonProperty
    private String tacticalRoles;
}
