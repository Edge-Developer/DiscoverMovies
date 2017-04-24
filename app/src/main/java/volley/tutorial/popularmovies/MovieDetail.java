package volley.tutorial.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import volley.tutorial.popularmovies.databinding.ActivityMovieDetailBinding;

import static volley.tutorial.popularmovies.RecyclerViewAdapter.BASE_IMAGE_URL;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMovieDetailBinding mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);

        setSupportActionBar(mDetailBinding.movieDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                String title = intent.getStringExtra(Intent.EXTRA_TEXT);
                Result movie = Singleton.getInstance().getMovie(title);
                if (movie != null) {
                    mDetailBinding.setMovie(movie);
                    mDetailBinding.movieThumbnail.setImageUrl(BASE_IMAGE_URL + movie.getPosterPath(), ConnectionManager.getImageLoader(this));
                }
            }
        }
    }
}
