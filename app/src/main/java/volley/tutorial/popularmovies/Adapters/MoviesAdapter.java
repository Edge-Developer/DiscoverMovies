package volley.tutorial.popularmovies.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.POJO.Result;
import volley.tutorial.popularmovies.databinding.SingleMovieCardBinding;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/18/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {

    public final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final Context context;
    private List<Result> mMovieResults;
    private ClickedMovie clicked_movie;

    public MoviesAdapter(Context context) {
        this.context = context;
        mMovieResults = new ArrayList<>();
        clicked_movie = (ClickedMovie) context;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleMovieCardBinding binding = DataBindingUtil.inflate(inflater, R.layout.single_movie_card, parent, false);
        return new MovieHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bind(mMovieResults.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieResults.size();
    }

    public void addMovies(List<Result> manyMovies) {

        mMovieResults = manyMovies;
        notifyDataSetChanged();
    }

    public interface ClickedMovie {
        void itemClicked(double movieID);
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private NetworkImageView moviePoster;
        private RatingBar mRatingBar;
        private TextView movieTitle;
        private Result movie;

        public MovieHolder( SingleMovieCardBinding binding) {
            super(binding.getRoot());
            moviePoster = (binding.moviePoster);
            mRatingBar = (binding.ratingBar);
            movieTitle = (binding.movieTitle);
            binding.getRoot().setOnClickListener(this);
        }

        private void bind(Result singleMovie) {
            movie = singleMovie;
            moviePoster.setImageUrl(BASE_IMAGE_URL + movie.getPosterPath(), ConnectionManager.getImageLoader(context));
            movieTitle.setText(movie.getOriginalTitle());
            mRatingBar.setRating(movie.getVoteAverage());

        }

        @Override
        public void onClick(View v) {
            clicked_movie.itemClicked(movie.getMovieId());
        }
    }
}
