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
public class IdmUsers {

    @JsonProperty("result")
    public List<IdmUser> users;

    @JsonProperty("resultCount")
    public int resultCount;

    @JsonProperty("pagedResultsCookie")
    public String pagedResultsCookie;

    @JsonProperty("totalPagedResultsPolicy")
    public String totalPagedResultsPolicy;

    @JsonProperty("totalPagedResults")
    public int totalPagedResults;

    @JsonProperty("remainingPagedResults")
    public int remainingPagedResults;

}
