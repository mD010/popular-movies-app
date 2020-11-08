package com.ydeng.popular_movies_app.movie_db.service;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.ydeng.popular_movies_app.MainActivity;
import com.ydeng.popular_movies_app.movie_db.impl.MovieServiceFactory;
import com.ydeng.popular_movies_app.movie_db.model.Movie;
import com.ydeng.popular_movies_app.movie_db.model.SortCriteria;
import com.ydeng.popular_movies_app.utils.NetworkUtils;

import net.minidev.json.JSONArray;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ydeng.popular_movies_app.utils.NetworkUtils.buildUrl;
import static com.ydeng.popular_movies_app.utils.NetworkUtils.getResponseFromHttpUrl;

public class LoadMoviesTask extends AsyncTask<SortCriteria, Void, String> {
    private MainActivity activity;
    private static final String TAG = "LoadMoviesTask";
    private static final String RESULTS_KEY = "results";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String ORIGINAL_LANGUAGE = "original_language";

    private ProgressBar mProgressBar;

    public LoadMoviesTask() {
        this.activity = MainActivity.getInstance();
        mProgressBar = activity.getProgressBar();
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: started...");
        super.onPreExecute();
        mProgressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "onPreExecute: progress bar is on");
    }

    @Override
    protected String doInBackground(SortCriteria... params) {
        Log.d(TAG, "doInBackground: started...");
        if (params.length == 0) {
            return null;
        }

        SortCriteria criteria = params[0];
        URL moviesUrl = buildUrl(criteria.getSubUrl());

        try {
            String getMoviesResponse = getResponseFromHttpUrl(moviesUrl);
            Log.d(TAG, "doInBackground: response=" + getMoviesResponse);
            if (getMoviesResponse == null || getMoviesResponse.trim().equals("")) {
                return null;
            }

            return getMoviesResponse;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String movieData) {
        Log.d(TAG, "onPostExecute: started...");
        super.onPostExecute(movieData);
        if (movieData == null || movieData.trim().length() == 0) {
            Log.w(TAG, "onPostExecute: movieData is empty...");
            mProgressBar.setVisibility(View.GONE);
            return;
        }

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(movieData);
        Log.d(TAG, "onPostExecute: document=" + document.toString());
        JSONArray movies = JsonPath.read(document, "$." + RESULTS_KEY);
        Log.d(TAG, "onPostExecute: movies: " + movies.toString());
        if (movies == null) {
            Log.d(TAG, "onPostExecute: movies is null. Skip deserializing...");
            return;
        }

        final List<Movie> movieList = Lists.newArrayList();
        movies.forEach(movie -> {
            movieList.add(toMovie((Map<String, Object>) movie));
        });

        Map<String, Movie> movieMap = movieList.stream()
                .collect(Collectors.toMap(Movie::getOriginalTitle, Function.identity(),
                        (oldValue, newValue) -> oldValue));
        MovieServiceFactory.getInstance().setMovies(movieMap);
        activity.updateUI(movieList);
        mProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onPostExecute: progress bar is gone...");
    }

    private Movie toMovie(Map<String, Object> map) {
        Movie movie = new Movie();
        movie.setImageUrl(NetworkUtils.buildImageUrlString((String) map.get(POSTER_PATH)));
        movie.setOriginalTitle((String) map.get(ORIGINAL_TITLE));
        movie.setOverview((String) map.get(OVERVIEW));
        movie.setReleaseDate((String) map.get(RELEASE_DATE));
        movie.setOriginalLanguage((String) map.get(ORIGINAL_LANGUAGE));
        if (map.get(VOTE_AVERAGE) instanceof Integer) {
            movie.setVoteAverage(Double.valueOf((Integer) map.get(VOTE_AVERAGE)));
        } else {
            movie.setVoteAverage((Double) map.get(VOTE_AVERAGE));
        }
        return movie;
    }
}
