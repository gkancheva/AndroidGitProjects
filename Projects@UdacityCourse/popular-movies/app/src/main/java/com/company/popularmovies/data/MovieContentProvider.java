package com.company.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {
    private static final int MOVIES = 100; //convention for matching whole table
    private static final int MOVIES_WITH_ID = 101; //convention for single item

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper sMovieDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        this.sMovieDBHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selCriteria, @Nullable String[] selArguments, @Nullable String sortOrder) {
        final SQLiteDatabase db = sMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIES:
                cursor = db.query(MovieDBEntry.TABLE_NAME, projection, selCriteria,
                        selArguments, null, null, sortOrder);
                break;
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String criteria = "_id = ?";
                String[] arguments = new String[]{id};
                cursor = db.query(MovieDBEntry.TABLE_NAME, projection, criteria,
                        arguments, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //not needed for the moment
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        final SQLiteDatabase db = sMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES:
                long id = db.insert(MovieDBEntry.TABLE_NAME, null, cv);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(MovieDBEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String whereClause, @Nullable String[] whereArgs) {
        final SQLiteDatabase db = sMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedRows;
        switch (match) {
            case MOVIES:
                deletedRows = db.delete(MovieDBEntry.TABLE_NAME, whereClause, whereArgs);
                break;
            case MOVIES_WITH_ID:
                String idToDelete = uri.getPathSegments().get(1);
                String clause = "_id = ?";
                String[] args = new String[]{idToDelete};
                deletedRows = db.delete(MovieDBEntry.TABLE_NAME, clause, args);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues cv, @Nullable String s, @Nullable String[] strings) {
        //not needed for the moment
        return 0;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_SINGLE_MOVIE, MOVIES_WITH_ID);
        return uriMatcher;
    }
}