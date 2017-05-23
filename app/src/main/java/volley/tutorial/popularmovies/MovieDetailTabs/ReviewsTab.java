package volley.tutorial.popularmovies.MovieDetailTabs;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import volley.tutorial.popularmovies.Adapters.ReviewAdapter;
import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.POJO.ReviewResults;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.ReviewsTrailerLayoutBinding;

import static android.support.design.widget.Snackbar.make;
import static volley.tutorial.popularmovies.Activities.MainActivity.API_KEY;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class ReviewsTab extends android.support.v4.app.Fragment {

    public static final String REVEWS_MOVIE_ID_KEY = "review_movie_id_key";
    private ReviewAdapter mAdapter;
    private ReviewsTrailerLayoutBinding binding;
    private double movieID;
    private ProgressBar mProgressBar;
    private Button retry;

    public ReviewsTab newInstance(double movieID) {
        Bundle args = new Bundle();
        args.putDouble(REVEWS_MOVIE_ID_KEY, movieID);
        ReviewsTab fragment = new ReviewsTab();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieID = getArguments().getDouble(REVEWS_MOVIE_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.reviews_trailer_layout, container, false);

        RecyclerView recyclerView = binding.reviewNTrailerRecyclerView;
        mProgressBar = binding.progressBar;
        retry = binding.retryButton;

        retry.setEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new ReviewAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                retry.setVisibility(View.VISIBLE);
                retry.setEnabled(false);
                downloadJSON(buildURL());

            }
        });
        downloadJSON(buildURL());
        return binding.getRoot();
    }

    private void downloadJSON(String JSON_URL) {
        final StringRequest mStringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ReviewResults reviews = gson.fromJson(response, ReviewResults.class);
                        mAdapter.addReviews(reviews.getResults());
                        mProgressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        make(binding.getRoot(), getString(R.string.error_message_toast), Snackbar.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.GONE);
                        retry.setVisibility(View.VISIBLE);
                        retry.setEnabled(true);
                    }
                });

        ConnectionManager.getInstance(getActivity()).add(mStringRequest);

    }

    private String buildURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + movieID)
                .appendPath("reviews")
                .appendQueryParameter("api_key", API_KEY);
        return builder.build().toString();
    }
}
