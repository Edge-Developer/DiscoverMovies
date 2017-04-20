package volley.tutorial.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickedMovie {

    private final String API_KEY = "";
    private String POPULAR_MOVIE_TYPE = "popular";
    private String HIGHLY_RATED_MOVIE_TYPE = "top_rated";
    private final String LANGUAGE = "en-US";
    private String MOVIE_TYPE = POPULAR_MOVIE_TYPE;
    private final int CURRENT_PAGE = 1;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayout;
    private RecyclerViewAdapter mAdapter;
    private Uri.Builder builder;
    private AccessJson getJSONdata;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Popular Movies");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int columns = new Utility().CalculateNoOfColumns(this);
        gridLayout = new GridLayoutManager(MainActivity.this, columns);
        mRecyclerView.setLayoutManager(gridLayout);
        mRecyclerView.setHasFixedSize(true);
        builder = new Uri.Builder();

        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        networkCall();
    }

    private void networkCall() {
        //dummyData();
        getJSONdata = new AccessJson();
        getJSONdata.DownloadJSON(buildUrl());
    }

    private String buildUrl() {
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
                toolbar.setTitle(getString(R.string.popular_movies_string));
                MOVIE_TYPE = POPULAR_MOVIE_TYPE;
                networkCall();
                break;
            case R.id.top_rated_movies:
                if (!item.isChecked()) {
                    item.setChecked(true);
                }
                toolbar.setTitle(getString(R.string.top_rated_string));
                MOVIE_TYPE = HIGHLY_RATED_MOVIE_TYPE;
                networkCall();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(String movieTitle) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieTitle);
        startActivity(intent);
    }

    class Utility {
        int CalculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumnns = (int) (dpWidth / 180);
            return noOfColumnns;
        }
    }

    class AccessJson {
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
                            Toast.makeText(MainActivity.this, getString(R.string.error_message_toast), Toast.LENGTH_LONG).show();
                        }
                    });

            ConnectionManager.getInstance(MainActivity.this).add(mStringRequest);
        }
    }

   /*public void dummyData(){

       List<Result> movies = new ArrayList<>();
       Result movie;

       for (int i=0; i<20;i++){
           movie = new Result(
                   "/tWqifoYuwLETmmasnGHO7xBjEtt.jpg"
                   ,getString(R.string.default_overview)
                   ,"2017-03-16"
                   ,"Beauty and the Beast"+i
                   ,150.167688
                   ,1690
                   ,6.9);

           movies.add(movie);
       }

       mAdapter.addMovies(movies);
       Singleton.getInstance().storeMovies(movies);
   }*/
}