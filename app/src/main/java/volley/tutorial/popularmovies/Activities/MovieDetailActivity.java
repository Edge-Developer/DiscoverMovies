package volley.tutorial.popularmovies.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.DB_Helper;
import volley.tutorial.popularmovies.MovieContract;
import volley.tutorial.popularmovies.MovieDetailTabs.OverviewTab;
import volley.tutorial.popularmovies.MovieDetailTabs.ReviewsTab;
import volley.tutorial.popularmovies.MovieDetailTabs.TrailersTab;
import volley.tutorial.popularmovies.POJO.Result;
import volley.tutorial.popularmovies.POJO.Singleton;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.ActivityMovieDetailBinding;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.support.design.widget.Snackbar.make;
import static volley.tutorial.popularmovies.Adapters.MoviesAdapter.BASE_IMAGE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_ID_KEY = "md_movie_key";
    public static final String FAV_ID_KEY = "fav_movie_key";
    private static final String TAG = "MovieDetailActivity";
    private ViewPager viewPager;
    private double movieID;
    private ActivityMovieDetailBinding mDetailBinding;
    private FloatingActionButton fab;
    private boolean movieState;
    private Result movie;

    private SQLiteDatabase mDatabase;
    private DB_Helper mDB_helper;


    public static Intent newIntent(Context context, double movieID, boolean favMovies) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieID);
        intent.putExtra(FAV_ID_KEY, favMovies);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mDB_helper = new DB_Helper(this);

        setSupportActionBar(mDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolbarLayout = mDetailBinding.collapsingToolbar;

        movieState = getIntent().getBooleanExtra(FAV_ID_KEY, false);
        movieID = getIntent().getDoubleExtra(MOVIE_ID_KEY, 0);

        fab = mDetailBinding.fab;

        if (movieState) {
            fab.setVisibility(View.GONE);
        } else {
            movie = Singleton.getInstance().getMovie(movieID);
        }

        String movieTitle = movie.getOriginalTitle();
        String movieThumb = movie.getPosterPath();

        toolbarLayout.setTitle(movieTitle);

        NetworkImageView networkImageView = mDetailBinding.movieThumbnail;
        networkImageView.setImageUrl(BASE_IMAGE_URL + movieThumb, ConnectionManager.getImageLoader(this));

        TabLayout tabLayout = mDetailBinding.detailTabs;
        viewPager = mDetailBinding.viewpager;

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.overview)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.reviews)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.trailer)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentStatePagerAdapter fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {
                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favMovie(Singleton.getInstance().getMovie(movieID));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void favMovie(Result movie) {
        mDatabase = mDB_helper.getWritableDatabase();
        boolean isInDB = false;

        if (mDatabase == null) {
            Log.e(TAG, " ----------> favMovie: LOL :)<------------");
            return;
        }
        List<Result> storedMovies = getMoviesFromDB();
        for (Result film : storedMovies) {
            if (movie.getMovieId() == film.getMovieId()) {
                isInDB = true;
                make(mDetailBinding.getRoot(), getString(R.string.already_added_to_fav), LENGTH_LONG).show();
            }
        }
        if (!isInDB) {
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "" + movie.getVoteAverageDouble());
            cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCountInt());
            try {
                mDatabase.beginTransaction();
                mDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
                mDatabase.setTransactionSuccessful();
            } catch (SQLException e) {
                make(mDetailBinding.getRoot(), e.getMessage(), LENGTH_LONG).show();
            } finally {
                mDatabase.endTransaction();
                make(mDetailBinding.getRoot(), getString(R.string.added_to_fav), LENGTH_LONG).show();

            }
        }

    }

    public List<Result> getMoviesFromDB() {
        Cursor cursor = MainActivity.getAllMovies();

        List<Result> movies = new ArrayList<>();
        Result movie;
        while (cursor.moveToNext()) {
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
        return movies;
    }

    public class FragmentAdapterClass extends FragmentStatePagerAdapter {
        int TabCount;

        public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {
            super(fragmentManager);
            this.TabCount = CountTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new OverviewTab().newInstance(movieID);
                case 1:
                    return new ReviewsTab().newInstance(movieID);
                case 2:
                    return new TrailersTab().newInstance(movieID);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TabCount;
        }
    }
}