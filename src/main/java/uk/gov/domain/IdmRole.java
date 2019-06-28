package uk.gov.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by efthymios.kartsonakis on 24/07/2017.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdmRole {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAssignableRoles() {
        return assignableRoles;
    }

    public void setAssignableRoles(List<String> assignableRoles) {
        this.assignableRoles = assignableRoles;
    }

    public List<String> getConflictingRoles() {
        return conflictingRoles;
    }

    public void setConflictingRoles(List<String> conflictingRoles) {
        this.conflictingRoles = conflictingRoles;
    }

    @JsonProperty
    private String _rev;

    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    @JsonProperty
    private List<String> assignableRoles;

    @JsonProperty
    private List<String> conflictingRoles;
}
