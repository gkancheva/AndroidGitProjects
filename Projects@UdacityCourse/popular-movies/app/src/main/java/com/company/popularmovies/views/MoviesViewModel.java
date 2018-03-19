package com.company.popularmovies.views;

import android.arch.lifecycle.ViewModel;

import com.company.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";

    private List<Movie> mPopularMovies = new ArrayList<>();
    private List<Movie> mTopRatedMovies = new ArrayList<>();
    private List<Movie> mFavouriteMovies = new ArrayList<>();

    public List<Movie> getMovies(String order) {
        switch (order) {
            case POPULAR:
                return this.mPopularMovies;
            case TOP_RATED:
                return this.mTopRatedMovies;
            default:
                return this.mFavouriteMovies;
        }
    }

    public void setMovies(List<Movie> movies, String order) {
        switch (order) {
            case POPULAR:
                this.mPopularMovies = movies;
                break;
            case TOP_RATED:
                this.mTopRatedMovies = movies;
                break;
            default:
                this.mFavouriteMovies = movies;
                break;
        }
    }

    public void addMovies(List<Movie> movies, String order) {
        if(order.equals(POPULAR)) {
            this.mPopularMovies.addAll(movies);
        } else if(order.equals(TOP_RATED)) {
            this.mTopRatedMovies.addAll(movies);
        }
    }

    public int getPage(String order) {
        switch (order) {
            case POPULAR:
                return this.mPopularMovies.size() / 20;
            case TOP_RATED:
                return this.mTopRatedMovies.size() / 20;
            default:
                return this.mFavouriteMovies.size() / 20;
        }
    }
}