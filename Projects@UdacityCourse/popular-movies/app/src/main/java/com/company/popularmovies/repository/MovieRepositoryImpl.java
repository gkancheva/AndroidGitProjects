package com.company.popularmovies.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.company.popularmovies.BuildConfig;
import com.company.popularmovies.R;
import com.company.popularmovies.data.MovieDBEntry;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.services.MovieLoaderTask;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.util.JsonUtils;
import com.company.popularmovies.util.NetworkUtils;
import com.company.popularmovies.util.StringUtil;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository,
        LoaderManager.LoaderCallbacks<String> {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String PAGE_SUFFIX = "&page=";
    private static final String PATH = "path";
    private static final int LOADER_ID = 123;
    private MovieRepoListener mListener;
    private Context mContext;
    private int mTotalPages;
    private int mCurrentPage;
    private String mCurrentOrder;
    private LoaderManager mLoaderManager;

    public MovieRepositoryImpl(MovieRepoListener listener, Context ctx, LoaderManager loaderManager) {
        this.mListener = listener;
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
        this.mCurrentPage = 0;
        this.mTotalPages = -1;
    }

    @Override
    public void getMovies(String order) {
        if(this.mCurrentOrder == null) {
            this.mCurrentOrder = order;
        }
        if(!this.mCurrentOrder.equals(order)) {
            this.mCurrentPage = 0;
            this.mCurrentOrder = order;
        }
        if(this.mTotalPages != -1) {
            if(this.mCurrentPage >= this.mTotalPages || this.mCurrentPage < 0) {
                return;
            }
        }
        String path = StringUtil.formatPath(
                mContext.getString(R.string.db_url_format),
                order, API_KEY) + PAGE_SUFFIX + ++this.mCurrentPage;
        Bundle args = new Bundle();
        args.putString(PATH, path);
        this.mLoaderManager.initLoader(LOADER_ID, args, this);
    }

    @Override
    public Movie findByIdFromDB(long id) {
        Cursor cursor = this.mContext.getContentResolver().query(MovieDBEntry.getContentUriSingleRow(id),
                null, null, null, null);
        Movie movie = null;
        if(cursor != null && cursor.moveToFirst()) {
            movie = this.getOneRowFromCursor(cursor);
            cursor.close();
        }
        return movie;
    }

    @Override
    public void getFavourites() {
        Cursor c = getFavouriteMovies();
        List<Movie> movies = new ArrayList<>();
        while(c.moveToNext()) {
            movies.add(this.getOneRowFromCursor(c));
        }
        c.close();
        mListener.onMoviesSuccess(movies);
    }

    @Override
    public boolean isFavourite(long id) {
        return this.findByIdFromDB(id) != null;
    }

    @Override
    public void addToFavourites(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieDBEntry._ID, movie.getId());
        cv.put(MovieDBEntry.COLUMN_NAME, movie.getOriginalName());
        cv.put(MovieDBEntry.COLUMN_OVERVIEW, movie.getOverview());
        String thumbnailPath = movie.getThumbnailPath();
        try {
            byte[] thumbnail = NetworkUtils.getBitmapFromURL(thumbnailPath);
            cv.put(MovieDBEntry.COLUMN_THUMBNAIL, thumbnail);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cv.put(MovieDBEntry.COLUMN_RATING, movie.getRating());
        cv.put(MovieDBEntry.COLUMN_TIMESTAMP, movie.getReleaseDate().getTime());
        Uri uri = this.mContext.getContentResolver().insert(MovieDBEntry.CONTENT_URI, cv);
        if(uri != null) {
            this.mListener.onMoviesSuccess(Collections.singletonList(movie));
            return;
        }
        this.mListener.onMoviesFailure();
    }

    @Override
    public void removeFromFavourite(long id) {
        Movie movie = this.findByIdFromDB(id);
        int deletedRows = this.mContext.getContentResolver()
                .delete(MovieDBEntry.getContentUriSingleRow(id), null, null);
        if(deletedRows > 0) {
            this.mListener.onMoviesSuccess(Collections.singletonList(movie));
        } else {
            this.mListener.onMoviesFailure();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        String path = bundle.getString(PATH);
        return new MovieLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null) {
            this.mListener.onMoviesFailure();
            this.mLoaderManager.destroyLoader(LOADER_ID);
            return;
        }
        try {
            this.mTotalPages = JsonUtils.getTotalPages(data, this.mContext);
            List<Movie> movies = JsonUtils.convertToMovieListObject(data, this.mContext);
            this.mListener.onMoviesSuccess(movies);
            this.mLoaderManager.destroyLoader(LOADER_ID);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.mListener.onMoviesFailure();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private Cursor getFavouriteMovies() {
        return this.mContext.getContentResolver().query(MovieDBEntry.CONTENT_URI,
                null, null, null, null);
    }

    private Movie getOneRowFromCursor(Cursor c) {
        long id = c.getLong(c.getColumnIndex(MovieDBEntry._ID));
        String name = c.getString(c.getColumnIndex(MovieDBEntry.COLUMN_NAME));
        byte[] thumbnailAsBytes = c.getBlob(c.getColumnIndex(MovieDBEntry.COLUMN_THUMBNAIL));
        Bitmap thumbnail = BitmapFactory.decodeByteArray(thumbnailAsBytes, 0, thumbnailAsBytes.length);
        String overview = c.getString(c.getColumnIndex(MovieDBEntry.COLUMN_OVERVIEW));
        double rating = c.getDouble(c.getColumnIndex(MovieDBEntry.COLUMN_RATING));
        long timestamp = c.getLong(c.getColumnIndex(MovieDBEntry.COLUMN_TIMESTAMP));
        Date releaseDate = new Date(timestamp);
        return new Movie(id, name, null, thumbnail, overview, rating, releaseDate);
    }

}