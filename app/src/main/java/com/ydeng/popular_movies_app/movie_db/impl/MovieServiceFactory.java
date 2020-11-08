package com.ydeng.popular_movies_app.movie_db.impl;

import com.ydeng.popular_movies_app.movie_db.service.MovieService;

public class MovieServiceFactory {
    public static MovieService getInstance() {
        MovieService movieService = MovieServiceImpl.getInstance();
        return movieService;
    }
}
