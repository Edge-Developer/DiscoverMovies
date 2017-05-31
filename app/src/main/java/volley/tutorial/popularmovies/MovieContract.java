package volley.tutorial.popularmovies;

import android.provider.BaseColumns;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/30/2017.
 */

public class MovieContract {
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

    }
}
