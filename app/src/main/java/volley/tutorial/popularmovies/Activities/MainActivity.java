package volley.tutorial.popularmovies.Activities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import volley.tutorial.popularmovies.Adapters.MoviesAdapter;
import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.DB_Helper;
import volley.tutorial.popularmovies.MovieContract;
import volley.tutorial.popularmovies.POJO.Movies;
import volley.tutorial.popularmovies.POJO.Result;
import volley.tutorial.popularmovies.POJO.Singleton;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.ActivityMainBinding;

import static android.support.design.widget.Snackbar.make;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.ClickedMovie {

    private static final String TAG = "MainActivity";

    public static final String API_KEY = "";
    private final String TOOLBAR_SUB_T_KEY = "subtitle_key";
    private final String POPULAR_MOVIE_TYPE = "popular";
    private final String TOP_RATED_MOVIE_TYPE = "top_rated";
    private final String LANGUAGE = "en-US";
    private final int CURRENT_PAGE = 1;
    private String MOVIE_TYPE = POPULAR_MOVIE_TYPE;
    private MoviesAdapter mAdapter;
    private Toolbar toolbar;
    private ActivityMainBinding mBinding;

    private static SQLiteDatabase mDatabase;
    private static DB_Helper db_helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        db_helper = new DB_Helper(this);

        toolbar = mBinding.mainactivityToolbar;
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            toolbar.setSubtitle(savedInstanceState.getString(TOOLBAR_SUB_T_KEY));
        } else {
            toolbar.setSubtitle(getString(R.string.popular_movies));
        }

        int columns = new Utility().CalculateNoOfColumns(this);
        GridLayoutManager gridLayout = new GridLayoutManager(MainActivity.this, columns);
        RecyclerView recyclerView = mBinding.recyclerView;
        recyclerView.setLayoutManager(gridLayout);
        recyclerView.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this, false);
        recyclerView.setAdapter(mAdapter);
        networkCall();
    }

    private void networkCall() {
        AccessJson getJSONdata = new AccessJson();
        getJSONdata.DownloadJSON(buildUrl());
    }

    private String buildUrl() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(MOVIE_TYPE)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", LANGUAGE)
                .appendQueryParameter("page", "" + CURRENT_PAGE);

        return builder.build().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.refresh:
                networkCall();
                mAdapter.setState(false);
                break;
            case R.id.popular_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setSubtitle(getString(R.string.popular_movies));
                MOVIE_TYPE = POPULAR_MOVIE_TYPE;
                networkCall();
                mAdapter.setState(false);
                break;
            case R.id.top_rated_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setSubtitle(getString(R.string.top_rated));
                MOVIE_TYPE = TOP_RATED_MOVIE_TYPE;
                networkCall();
                mAdapter.setState(false);
                break;
            case R.id.favorite_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                Result movie;
                Cursor cursor = getAllMovies();
                Log.e(TAG, "onOptionsItemSelected: "+cursor.getCount() );
                List<Result> movies = new ArrayList<>();
                while (cursor.moveToNext()){

                    String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                    int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                    String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                    String poster_path = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                    String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                    String vote_average = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
                    int vote_count = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));

                    movie = new Result();
                    movie.setOriginalTitle(title);
                    movie.setMovieId(id);
                    movie.setPosterPath(poster_path);
                    movie.setReleaseDate(release_date);
                    movie.setOverview(overview);
                    movie.setVoteCount(vote_count);
                    movie.setVoteAverage(Double.parseDouble(vote_average));
                    movies.add(movie);
                }
                cursor.close();
                mAdapter.addMovies(movies);
                toolbar.setSubtitle(getString(R.string.favorite_movies));
                mAdapter.setState(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(double movieID, boolean state) {
        startActivity(MovieDetailActivity.newIntent(this, movieID, state));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TOOLBAR_SUB_T_KEY, toolbar.getSubtitle().toString());
    }

    public static Cursor getAllMovies() {
        mDatabase = db_helper.getReadableDatabase();
        return mDatabase.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

    }

    private class Utility {
        int CalculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 180);
        }
    }

    public class AccessJson {
        void DownloadJSON(String JSON_URL) {
            StringRequest mStringRequest = new StringRequest(JSON_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Movies movies = gson.fromJson(response, Movies.class);
                            mAdapter.addMovies(movies.getResults());
                            Singleton.getInstance().storeMovies(movies.getResults());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            make(mBinding.getRoot(), getString(R.string.error_message_toast), Snackbar.LENGTH_LONG).show();
                        }
                    });
            ConnectionManager.getInstance(MainActivity.this).add(mStringRequest);
        }
    }
}