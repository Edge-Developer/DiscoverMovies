package volley.tutorial.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/18/2017.
 */

public class Result {
    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    private Double voteAverage;

    public Result(String posterPath, String overview, String releaseDate, String originalTitle, Double popularity, Integer voteCount, Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {

        String dDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date date = simpleDateFormat.parse(releaseDate);
            simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            dDate = simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getVoteCount() {
        return  voteCount+" ratings";
    }

    public float getVoteAverage() {
        return (float)(voteAverage/2);
    }
    public String getVoteAverage1() {
        return ""+voteAverage/2;
    }
}
