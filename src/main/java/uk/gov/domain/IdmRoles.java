package uk.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdmRoles {

    @JsonProperty("result")
    public List<IdmRole> roles;

    public List<IdmRole> getRoles() {
        return roles;
    }

    public void setRoles(List<IdmRole> roles) {
        this.roles = roles;
    }
}
