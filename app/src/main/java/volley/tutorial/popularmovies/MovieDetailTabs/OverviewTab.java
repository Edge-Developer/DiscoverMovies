package volley.tutorial.popularmovies.MovieDetailTabs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import volley.tutorial.popularmovies.POJO.Result;
import volley.tutorial.popularmovies.POJO.Singleton;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.LayoutMovieOverviewBinding;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class OverviewTab extends android.support.v4.app.Fragment {

    private static final String ARG_MOVIE_ID_KEY = "movie_id_key";
    private Result mMovie;

    public OverviewTab newInstance(double movieID) {
        Bundle args = new Bundle();
        args.putDouble(ARG_MOVIE_ID_KEY, movieID);
        OverviewTab fragment = new OverviewTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        double movieID = getArguments().getDouble(ARG_MOVIE_ID_KEY);
        mMovie = Singleton.getInstance().getMovie(movieID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutMovieOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_movie_overview, container, false);
        binding.setMovie(mMovie);
        return binding.getRoot();
    }
}
