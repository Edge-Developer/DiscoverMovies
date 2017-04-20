package volley.tutorial.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static volley.tutorial.popularmovies.RecyclerViewAdapter.BASE_IMAGE_URL;

public class MovieDetail extends AppCompatActivity {

    private NetworkImageView thumbnail;
    private TextView overview;
    private TextView textRating;
    private TextView releaseDate;
    private CollapsingToolbarLayout mToolbarLayout;

    private TextView mVoteCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        thumbnail = (NetworkImageView) findViewById(R.id.movie_thumbnail);
        overview = (TextView) findViewById(R.id.movie_plot);
        textRating = (TextView) findViewById(R.id.text_rating);
        releaseDate = (TextView) findViewById(R.id.release_date);
        mVoteCount = (TextView) findViewById(R.id.vote_count);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                String title = intent.getStringExtra(Intent.EXTRA_TEXT);
                Result movie = Singleton.getInstance().getMovie(title);
                if (null != movie) {
                    thumbnail.setImageUrl(BASE_IMAGE_URL + movie.getPosterPath(), ConnectionManager.getImageLoader(this));
                    mToolbarLayout.setTitle(title);
                    overview.setText(movie.getOverview());

                    textRating.setText(
                            String.format(
                                    "%s %s %s"
                                    , getString(R.string.rating_string)
                                    , movie.getVoteAverage()
                                    , getString(R.string.over_ten))
                    );
                    String dDate = null;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                        try {
                            Date date = simpleDateFormat.parse(movie.getReleaseDate());
                            simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                            dDate = simpleDateFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    releaseDate.setText(String.format("%s %s", getString(R.string.release_date), dDate));
                    mVoteCount.setText(getString(R.string.vote_count) +" "+movie.getVoteCount());
                }
            }
        }
    }

    public void fab(View view) {
        Toast.makeText(this, "You Tapped the Share Button", Toast.LENGTH_SHORT).show();
    }
}
