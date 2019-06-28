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

    public List<IdmUser> getUsers() {
        return users;
    }

    public void setUsers(List<IdmUser> users) {
        this.users = users;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public String getPagedResultsCookie() {
        return pagedResultsCookie;
    }

    public void setPagedResultsCookie(String pagedResultsCookie) {
        this.pagedResultsCookie = pagedResultsCookie;
    }

    public String getTotalPagedResultsPolicy() {
        return totalPagedResultsPolicy;
    }

    public void setTotalPagedResultsPolicy(String totalPagedResultsPolicy) {
        this.totalPagedResultsPolicy = totalPagedResultsPolicy;
    }

    public int getTotalPagedResults() {
        return totalPagedResults;
    }

    public void setTotalPagedResults(int totalPagedResults) {
        this.totalPagedResults = totalPagedResults;
    }

    public int getRemainingPagedResults() {
        return remainingPagedResults;
    }

    public void setRemainingPagedResults(int remainingPagedResults) {
        this.remainingPagedResults = remainingPagedResults;
    }

    @JsonProperty("pagedResultsCookie")
    public String pagedResultsCookie;

    @JsonProperty("totalPagedResultsPolicy")
    public String totalPagedResultsPolicy;

    @JsonProperty("totalPagedResults")
    public int totalPagedResults;

    @JsonProperty("remainingPagedResults")
    public int remainingPagedResults;

}
