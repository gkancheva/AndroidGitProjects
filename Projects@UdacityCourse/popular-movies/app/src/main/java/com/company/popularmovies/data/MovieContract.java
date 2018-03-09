package com.company.popularmovies.data;

import android.net.Uri;

public class MovieContract {
    public static final String AUTHORITY = "com.company.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_SINGLE_MOVIE = "movies/#";

}