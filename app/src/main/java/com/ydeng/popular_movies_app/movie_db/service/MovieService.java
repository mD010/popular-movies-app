package com.ydeng.popular_movies_app.movie_db.service;

import com.ydeng.popular_movies_app.movie_db.model.Movie;
import com.ydeng.popular_movies_app.movie_db.model.SortCriteria;

import java.util.Map;

public interface MovieService {

    void showMoviesBySortCriteria(SortCriteria sortCriteria);

    Map<String, Movie> getMovies();

    void setMovies(Map<String, Movie> movies);
}
