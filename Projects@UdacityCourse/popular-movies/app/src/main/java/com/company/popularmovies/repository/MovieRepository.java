package com.company.popularmovies.repository;

import com.company.popularmovies.models.Movie;

public interface MovieRepository {
    void getMovies(String order);
    void fetchNewPage(String order, int requestedPage);
    boolean isFavourite(Movie movie);
    void getFavourites(String order);
    void addToFavourites(Movie movie);
    void removeFromFavourite(Movie movie);
    void getTrailers(long movieId);
    void getReviews(long movieId);
}