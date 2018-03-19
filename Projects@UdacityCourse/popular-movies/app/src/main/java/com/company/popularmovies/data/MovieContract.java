package com.company.popularmovies.data;

import android.net.Uri;

class MovieContract {
    static final String AUTHORITY = "com.company.popularmovies";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_MOVIES = "movies";
    static final String PATH_SINGLE_MOVIE = "movies/#";

}