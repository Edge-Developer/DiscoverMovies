package volley.tutorial.popularmovies.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/19/2017.
 */

public class Singleton {
    private static List<Result> mMovies;
    private static Singleton mSingleton;

    private Singleton() {
        mMovies = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (null == mSingleton) {
            mSingleton = new Singleton();
        }
        return mSingleton;
    }

    public void storeMovies (List<Result> movies){
        mMovies = movies;
    }

    /*public Result getMovie(String title){
        for (Result movie : mMovies) {
            if (title.equalsIgnoreCase(movie.getOriginalTitle())){
              return movie;
            }
        }
        return null;
    }*/

    public Result getMovie(double id){
        for (Result movie : mMovies) {
            if (id == movie.getMovieId()){
              return movie;
            }
        }
        return null;
    }
}
