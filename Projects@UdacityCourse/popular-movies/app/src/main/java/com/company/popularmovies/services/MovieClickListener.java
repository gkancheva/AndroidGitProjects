package com.company.popularmovies.services;

import com.company.popularmovies.models.Movie;

public interface MovieClickListener {
    void onMovieClicked(Movie movie);
}