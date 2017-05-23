package volley.tutorial.popularmovies.Activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import io.realm.Realm;
import volley.tutorial.popularmovies.Adapters.MoviesAdapter;
import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.POJO.Movies;
import volley.tutorial.popularmovies.POJO.Singleton;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.ActivityMainBinding;

import static android.support.design.widget.Snackbar.make;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.ClickedMovie {

    public static final String API_KEY = "11f4275ea0f71297a1d33044b675828f";
    private final String POPULAR_MOVIE_TYPE = "popular";
    private final String TOP_RATED_MOVIE_TYPE = "top_rated";
    private final String LANGUAGE = "en-US";
    private final int CURRENT_PAGE = 1;
    private String MOVIE_TYPE = POPULAR_MOVIE_TYPE;
    private GridLayoutManager gridLayout;
    private MoviesAdapter mAdapter;
    private Toolbar toolbar;
    private ActivityMainBinding mBinding;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        toolbar = mBinding.mainactivityToolbar;
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(getString(R.string.popular_movies));

        int columns = new Utility().CalculateNoOfColumns(this);
        gridLayout = new GridLayoutManager(MainActivity.this, columns);
        mBinding.recyclerView.setLayoutManager(gridLayout);
        mBinding.recyclerView.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
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
                break;
            case R.id.popular_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setSubtitle(getString(R.string.popular_movies));
                MOVIE_TYPE = POPULAR_MOVIE_TYPE;
                networkCall();
                break;
            case R.id.top_rated_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setSubtitle(getString(R.string.top_rated));
                MOVIE_TYPE = TOP_RATED_MOVIE_TYPE;
                networkCall();
                break;
            case R.id.favorite_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setSubtitle(getString(R.string.favorite_movies));
                MOVIE_TYPE = TOP_RATED_MOVIE_TYPE;
                networkCall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(double movieID) {
        startActivity(MovieDetailActivity.newIntent(this, movieID));
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