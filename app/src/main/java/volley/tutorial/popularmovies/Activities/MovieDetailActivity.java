package volley.tutorial.popularmovies.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;

import io.realm.Realm;
import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.MovieDetailTabs.OverviewTab;
import volley.tutorial.popularmovies.MovieDetailTabs.ReviewsTab;
import volley.tutorial.popularmovies.MovieDetailTabs.TrailersTab;
import volley.tutorial.popularmovies.POJO.Result;
import volley.tutorial.popularmovies.POJO.Singleton;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.ActivityMovieDetailBinding;

import static android.support.design.widget.Snackbar.make;
import static volley.tutorial.popularmovies.Adapters.MoviesAdapter.BASE_IMAGE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_ID_KEY = "md_movie_key";
    private ViewPager viewPager;
    private double movieID;
    private ActivityMovieDetailBinding mDetailBinding;
    private FloatingActionButton fab;
    private Realm mRealm;
    private Result movie101;

    public static Intent newIntent(Context context, double movieID) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID_KEY, movieID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        mRealm = Realm.getDefaultInstance();

        movieID = getIntent().getDoubleExtra(MOVIE_ID_KEY, 0);
        Result movie = Singleton.getInstance().getMovie(movieID);
        String movieTitle = movie.getOriginalTitle();
        String movieThumb = movie.getPosterPath();

        setSupportActionBar(mDetailBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = mDetailBinding.fab;
        CollapsingToolbarLayout toolbarLayout = mDetailBinding.collapsingToolbar;
        toolbarLayout.setTitle(movieTitle);

        NetworkImageView networkImageView = mDetailBinding.movieThumbnail;
        networkImageView.setImageUrl(BASE_IMAGE_URL + movieThumb, ConnectionManager.getImageLoader(this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

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
                movie101 = Singleton.getInstance().getMovie(movieID);
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //long movie_db_id = realm.where(Result.class).max("id").longValue();

                        Result movie = realm.createObject(Result.class);
                        movie.setMovieId(movie101.getMovieId());
                        movie.setOriginalTitle(movie101.getOriginalTitle());
                        movie.setOverview(movie101.getOverview());
                        movie.setPosterPath(movie101.getPosterPath());
                        movie.setReleaseDate(movie101.getReleaseDate());
                        movie.setVoteAverage(movie101.getVoteAverageDouble());
                        movie.setVoteCount(movie101.getVoteCountInt());
                        //movie.setId((movie_db_id+1));
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        make(mDetailBinding.getRoot(),"Added To Favorite", Snackbar.LENGTH_LONG).show();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        make(mDetailBinding.getRoot(),"Movie Already Added!", Snackbar.LENGTH_LONG).show();

                    }
                });
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