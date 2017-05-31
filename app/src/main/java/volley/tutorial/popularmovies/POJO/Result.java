package volley.tutorial.popularmovies.POJO;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/18/2017.
 */

public class Result{


    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("id")
    private int movieId;

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {

        String dDate = null;
        try {
            Date date = new SimpleDateFormat("yyyy-mm-dd").parse(releaseDate);
            dDate = new SimpleDateFormat("MMMM dd, yyyy").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }


    public String getVoteCount() {

        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(voteCount) + " ratings";
    }

    public int getVoteCountInt() {
        return voteCount;
    }

    public float getVoteAverage() {
        return (float) (voteAverage / 2);
    }

    public double getVoteAverageDouble() {
        return voteAverage;
    }

    public String getVoteAverage1() {

        DecimalFormat df = new DecimalFormat("###.##");
        return "" + df.format(voteAverage / 2);
    }

    public int getMovieId() {
        return movieId;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}