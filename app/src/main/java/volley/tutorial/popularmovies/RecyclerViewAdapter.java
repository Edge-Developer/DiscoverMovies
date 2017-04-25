package volley.tutorial.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 4/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieHolder> {

    public final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private final Context context;
    private List<Result> mMovieResults;
    private ClickedMovie clicked_movie;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        mMovieResults = new ArrayList<>();
        clicked_movie = (ClickedMovie) context;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutResourceID = R.layout.single;
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutResourceID, parent, shouldAttachToParentImmediately);
        MovieHolder holder = new MovieHolder(view);

        return holder;
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

        /*for (Result movie : manyMovies) {
            mMovieResults.add(movie);
        }*/
    }

    public interface ClickedMovie {
        void itemClicked(String movieTitle);
    }

    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private NetworkImageView moviePoster;
        private RatingBar mRatingBar;
        private TextView movieTitle;
        private Result movie;

        public MovieHolder(View itemView) {
            super(itemView);
            moviePoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        private void bind(Result singleMovie) {
            movie = singleMovie;

            moviePoster.setImageUrl(BASE_IMAGE_URL + movie.getPosterPath(), ConnectionManager.getImageLoader(context));

            movieTitle.setText(movie.getOriginalTitle());

            mRatingBar.setRating(movie.getVoteAverage());

        }

        @Override
        public void onClick(View v) {
            clicked_movie.itemClicked(movie.getOriginalTitle());
        }
    }
}
