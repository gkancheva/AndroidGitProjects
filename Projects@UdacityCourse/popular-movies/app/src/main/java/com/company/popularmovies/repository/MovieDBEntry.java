package com.company.popularmovies.repository;

import android.provider.BaseColumns;

public final class MovieDBEntry implements BaseColumns {

    private MovieDBEntry(){}

    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_NAME = "original_name";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_TIMESTAMP = "release_date";
}