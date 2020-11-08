package com.ydeng.popular_movies_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.ydeng.popular_movies_app.movie_db.impl.MovieServiceFactory;
import com.ydeng.popular_movies_app.movie_db.model.Movie;
import com.ydeng.popular_movies_app.ui.RecyclerViewAdapter;
import com.ydeng.popular_movies_app.utils.ConnectionChecker;

import java.util.Map;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = "MovieDetailsActivity";
    private static MovieDetailsActivity mInstance;
    private ImageView mHeaderImageView;
    private TextView mRatingsTextView;
    private TextView mMovieTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mOriginalLanguageTextView;
    private TextView mDescriptionTextView;
    private ConnectionChecker connectionChecker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started");
        mInstance = this;
        setContentView(R.layout.activity_movie_details);
        connectionChecker = new ConnectionChecker(this);
        mHeaderImageView = findViewById(R.id.iv_header);
        mRatingsTextView = findViewById(R.id.tv_ratings);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_value);
        mOriginalLanguageTextView = findViewById(R.id.tv_original_language_value);
        mDescriptionTextView = findViewById(R.id.tv_movie_description);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!connectionChecker.checkConnection()) {
            return;
        }

        Map<String, Movie> movieMap = MovieServiceFactory.getInstance().getMovies();
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(RecyclerViewAdapter.MOVIE_TITLE_EXTRA) || movieMap.isEmpty()) {
            Log.w(TAG, "onCreate: cannot retrieve movie information...");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return;
        }

        String movieTitle = intent.getStringExtra(RecyclerViewAdapter.MOVIE_TITLE_EXTRA);
        Movie movie = movieMap.getOrDefault(movieTitle, null);
        if (movie == null) {
            Log.w(TAG, "onCreate: movie is null for title " + movieTitle);
            return;
        }

        Picasso.get()
                .load(movie.getImageUrl())
                .resize(135, 160)
                .into(mHeaderImageView);
        mMovieTitleTextView.setText(movie.getOriginalTitle());
        mRatingsTextView.setText(String.valueOf(movie.getVoteAverage()));
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mOriginalLanguageTextView.setText(movie.getOriginalLanguage());
        mDescriptionTextView.setText(movie.getOverview());
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: started...");
        super.onStart();
    }

    public static synchronized MovieDetailsActivity getInstance() {
        return mInstance;
    }
}
