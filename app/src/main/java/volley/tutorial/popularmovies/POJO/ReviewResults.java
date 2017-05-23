package volley.tutorial.popularmovies.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */


public class ReviewResults {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("page")
    @Expose
    private Integer page;

    @SerializedName("results")
    @Expose
    private List<Review> results = null;

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public List<Review> getResults() {
        return results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

}