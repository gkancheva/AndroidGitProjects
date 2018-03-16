package com.company.popularmovies.repository;

import com.company.popularmovies.models.Movie;

public interface MovieRepository {
    void getMovies(String order);
    Movie findByIdFromDB(long id);
    void getFavourites();
    boolean isFavourite(long id);
    void addToFavourites(Movie movie);
    void removeFromFavourite(long id);
    void getTrailers(long movieId);
    void getReviews(long movieId);
}