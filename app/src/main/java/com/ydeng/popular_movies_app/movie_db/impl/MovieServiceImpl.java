package com.ydeng.popular_movies_app.movie_db.impl;

import com.ydeng.popular_movies_app.movie_db.service.LoadMoviesTask;
import com.ydeng.popular_movies_app.movie_db.service.MovieService;
import com.ydeng.popular_movies_app.movie_db.model.Movie;
import com.ydeng.popular_movies_app.movie_db.model.SortCriteria;

import java.util.Map;

public class MovieServiceImpl implements MovieService {

    private Map<String, Movie> movies;

    private static class Loader {
        static volatile MovieServiceImpl INSTANCE = new MovieServiceImpl();
    }

    static MovieServiceImpl getInstance() {
        return MovieServiceImpl.Loader.INSTANCE;
    }

    @Override
    public void showMoviesBySortCriteria(SortCriteria sortCriteria) {
        switch (sortCriteria) {
            case POPULARITY:
                showPopularMovies();
                break;
            case TOP_RATED:
                showTopRatedMovies();
                break;
        }
    }

    private void showPopularMovies() {
        new LoadMoviesTask().execute(SortCriteria.POPULARITY);
    }

    private void showTopRatedMovies() {
        new LoadMoviesTask().execute(SortCriteria.TOP_RATED);
    }

    @Override
    public Map<String, Movie> getMovies() {
        return movies;
    }

    @Override
    public void setMovies(Map<String, Movie> movies) {
        this.movies = movies;
    }

}
