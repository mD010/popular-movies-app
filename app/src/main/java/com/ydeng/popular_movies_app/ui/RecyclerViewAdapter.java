package com.ydeng.popular_movies_app.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ydeng.popular_movies_app.MovieDetailsActivity;
import com.ydeng.popular_movies_app.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    public static final String MOVIE_TITLE_EXTRA = "movie_title";

    private List<String> mMovieTitles = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<String> mMovieTitles, List<String> mImages) {
        this.mMovieTitles = mMovieTitles;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Picasso.get()
                .load(mImages.get(position))
                .resize(135,160)
                .into(holder.image);

        holder.movieTitle.setText(mMovieTitles.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click on: " + mMovieTitles.get(position));

                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_TITLE_EXTRA, mMovieTitles.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView movieTitle;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            movieTitle = itemView.findViewById(R.id.movie_title);
            parentLayout = itemView.findViewById(R.id.layout_list_item);
        }
    }
}
