package volley.tutorial.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
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

    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("vote_average")
    private Double voteAverage;

    public Result(String posterPath, String overview, String releaseDate, String originalTitle, Integer voteCount, Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
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
        return  formatter.format(voteCount)+" ratings";
    }

    public float getVoteAverage() {
        return (float)(voteAverage/2);
    }
    public String getVoteAverage1() {

        DecimalFormat df = new DecimalFormat("###.##");
        return ""+df.format(voteAverage/2);
    }
}

/*
package com.mkyong;

        import java.text.DecimalFormat;

public class RoundValue
{
    public static void main(String[] args)
    {

        double kilobytes = 1205.6358;

        System.out.println("kilobytes : " + kilobytes);

        double newKB = Math.round(kilobytes*100.0)/100.0;
        System.out.println("kilobytes (Math.round) : " + newKB);

        DecimalFormat df = new DecimalFormat("###.##");
        System.out.println("kilobytes (DecimalFormat) : " + df.format(kilobytes));
    }
}*/
