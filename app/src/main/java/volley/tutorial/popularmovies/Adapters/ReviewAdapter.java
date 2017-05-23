package volley.tutorial.popularmovies.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.POJO.Review;
import volley.tutorial.popularmovies.databinding.SingleReviewBinding;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context mContext;
    private List<Review> mReviews = new ArrayList<>();

    public ReviewAdapter(Context context) {
        this.mContext = context;
        //mReviews = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleReviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_review, parent, false);
        return new ReviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void addReviews(List<Review> reviews) {
        for (Review r : reviews) {
            mReviews.add(r);
        }
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private SingleReviewBinding vBinding;

        public ReviewHolder(SingleReviewBinding binding) {
            super(binding.getRoot());
            vBinding = binding;
        }

        public void bind(Review review) {
            vBinding.setReview(review);
        }
    }
}
