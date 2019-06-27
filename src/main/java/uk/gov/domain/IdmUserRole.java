package uk.gov.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Created by efthymios.kartsonakis on 02/08/2017.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdmUserRole {

    @JsonProperty
    private String _ref;

    @JsonProperty
    private RefProperties _refProperties;

}
