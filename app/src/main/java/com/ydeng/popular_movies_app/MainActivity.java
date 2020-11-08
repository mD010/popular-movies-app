package com.ydeng.popular_movies_app;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Lists;
import com.ydeng.popular_movies_app.movie_db.service.MovieService;
import com.ydeng.popular_movies_app.movie_db.impl.MovieServiceFactory;
import com.ydeng.popular_movies_app.movie_db.model.Movie;
import com.ydeng.popular_movies_app.movie_db.model.SortCriteria;
import com.ydeng.popular_movies_app.ui.RecyclerViewAdapter;
import com.ydeng.popular_movies_app.utils.ConnectionChecker;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<String> mMovieTitles = Lists.newArrayList();
    private List<String> mImageUrls = Lists.newArrayList();

    private MovieService movieService;
    private SortCriteria sortCriteria = SortCriteria.POPULARITY;

    private static MainActivity mInstance;
    private ProgressBar mProgressBar;
    private ConnectionChecker connectionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;
        Log.d(TAG, "onCreate: started.");
        mProgressBar = findViewById(R.id.progressBar);
        connectionChecker = new ConnectionChecker(this);
        movieService = MovieServiceFactory.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started");
//        connectionChecker.checkConnection();
//        movieService.showMoviesBySortCriteria(sortCriteria);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: started");
        if (connectionChecker.checkConnection()) {
            movieService.showMoviesBySortCriteria(sortCriteria);
        }
    }

    public void updateUI(List<Movie> movieList) {
        Log.d(TAG, "initRecyclerView: init recylerview.");
        initImageBitmaps(movieList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mMovieTitles, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initImageBitmaps(List<Movie> movieList) {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        mImageUrls.clear();
        mMovieTitles.clear();
        for (Movie movie : movieList) {
            mImageUrls.add(movie.getImageUrl());
            mMovieTitles.add(movie.getOriginalTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_by_popularity) {
            Log.d(TAG, "onOptionsItemSelected: selected sort by popularity");
            sortCriteria = SortCriteria.POPULARITY;
            movieService.showMoviesBySortCriteria(sortCriteria);
            return true;
        }

        if (id == R.id.action_sort_by_top_rated) {
            Log.d(TAG, "onOptionsItemSelected: selected sort by top rated");
            sortCriteria = SortCriteria.TOP_RATED;
            movieService.showMoviesBySortCriteria(sortCriteria);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }
}