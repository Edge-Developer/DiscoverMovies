package volley.tutorial.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/18/2017.
 */

public class Movies {

    @SerializedName("page")
    private Integer page;

    @SerializedName("results")
    private List<Result> results = null;

    @SerializedName("total_results")
    private Integer totalResults;

    @SerializedName("total_pages")
    private Integer totalPages;


    public List<Result> getResults() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}