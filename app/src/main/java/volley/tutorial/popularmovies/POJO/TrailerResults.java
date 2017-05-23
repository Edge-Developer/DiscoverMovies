package volley.tutorial.popularmovies.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class TrailerResults {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public Integer getId() {
        return id;
    }
    public List<Trailer> getResults() {
        return results;
    }

}
