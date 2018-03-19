package com.company.popularmovies.services;

import com.company.popularmovies.models.Movie;

import java.util.List;

public interface MovieRepoListener {
    void onMoviesSuccess(List<Movie> movies);
    void onMoviesFailure(String message);
}