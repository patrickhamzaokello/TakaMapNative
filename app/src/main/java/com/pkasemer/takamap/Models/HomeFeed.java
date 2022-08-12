
package com.pkasemer.takamap.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class HomeFeed {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("infrastructure")
    @Expose
    private List<Infrastructure> infrastructure = null;
    @SerializedName("types")
    @Expose
    private List<Type> types = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Infrastructure> getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(List<Infrastructure> infrastructure) {
        this.infrastructure = infrastructure;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
