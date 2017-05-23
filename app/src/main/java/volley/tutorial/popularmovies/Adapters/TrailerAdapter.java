package volley.tutorial.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import volley.tutorial.popularmovies.Connection.ConnectionManager;
import volley.tutorial.popularmovies.POJO.Trailer;
import volley.tutorial.popularmovies.R;
import volley.tutorial.popularmovies.databinding.SingleTrailerBinding;

/**
 * Created by OPEYEMI OLORUNLEKE on 5/20/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private List<Trailer> mTrailers =  new ArrayList<>();
    private Context mContext;
    private static final String YOUTUBE_THUMB_URL = "http://i3.ytimg.com/vi/";



    public TrailerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SingleTrailerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.single_trailer, parent, false);
        return new TrailerHolder(binding);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.bind(mTrailers.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void addTrailers(List<Trailer> results) {
        for (Trailer trailer : results){
            mTrailers.add(trailer);
        }
        notifyDataSetChanged();
    }

    public class TrailerHolder extends ViewHolder implements View.OnClickListener {
        private SingleTrailerBinding vBinding;
        private NetworkImageView trailerThumbnail;
        private TextView trailerTitle;
        private String theUrl;

        public TrailerHolder(SingleTrailerBinding binding) {
            super(binding.getRoot());
            vBinding = binding;
            trailerThumbnail = vBinding.trailerImage;
            trailerTitle = vBinding.movieTrailerTitle;
            trailerTitle.setOnClickListener(this);
        }

        public void bind(Trailer trailer) {
            trailerTitle.setText(trailer.getName());
            theUrl = "https://www.youtube.com/watch?v=" +trailer.getKey();
            trailerThumbnail.setImageUrl(YOUTUBE_THUMB_URL + trailer.getKey()+ "/3.jpg", ConnectionManager.getImageLoader(mContext));
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(theUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(mContext.getPackageManager()) != null)
                mContext.startActivity(intent);
        }
    }
}
