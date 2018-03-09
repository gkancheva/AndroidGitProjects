package com.company.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieDBEntry implements BaseColumns {
    public static final Uri CONTENT_URI = MovieContract.BASE_CONTENT_URI
            .buildUpon().appendPath(MovieContract.PATH_MOVIES).build();

    private MovieDBEntry(){}

    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_NAME = "original_name";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_TIMESTAMP = "release_date";

    public static Uri getContentUriSingleRow(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

}