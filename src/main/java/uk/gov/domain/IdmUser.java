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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<IdmUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdmUserRole> roles) {
        this.roles = roles;
    }

    public String getTacticalRoles() {
        return tacticalRoles;
    }

    public void setTacticalRoles(String tacticalRoles) {
        this.tacticalRoles = tacticalRoles;
    }

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
