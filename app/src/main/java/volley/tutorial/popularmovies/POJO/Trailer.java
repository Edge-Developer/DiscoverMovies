package volley.tutorial.popularmovies.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class Trailer {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private Integer size;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }


    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }
}
