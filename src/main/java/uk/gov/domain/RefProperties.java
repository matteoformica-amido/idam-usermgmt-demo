package uk.gov.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefProperties {

    @JsonProperty
    private String _id;

    @JsonProperty
    private String _rev;

    @JsonProperty
    private String _grantType;

    @JsonProperty
    private List<?> temporalConstraints;

}
