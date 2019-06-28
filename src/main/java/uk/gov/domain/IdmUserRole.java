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

    public String get_ref() {
        return _ref;
    }

    public void set_ref(String _ref) {
        this._ref = _ref;
    }

    public RefProperties get_refProperties() {
        return _refProperties;
    }

    public void set_refProperties(RefProperties _refProperties) {
        this._refProperties = _refProperties;
    }

    @JsonProperty
    private RefProperties _refProperties;

}
