package com.company.popularmovies.services;

import com.company.popularmovies.models.Review;
import com.company.popularmovies.models.Trailer;

import java.util.List;

public interface TrailerReviewRepoListener {
    void onTrailersSuccess(List<Trailer> trailers);
    void onReviewsSuccess(List<Review> reviews);
}