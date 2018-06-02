package com.company.popularmovies.repository;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.company.popularmovies.BuildConfig;
import com.company.popularmovies.R;
import com.company.popularmovies.data.MovieDBEntry;
import com.company.popularmovies.data.MovieQueryHandler;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.models.Review;
import com.company.popularmovies.models.Trailer;
import com.company.popularmovies.services.MovieLoaderTask;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.services.TrailerReviewRepoListener;
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
        LoaderManager.LoaderCallbacks<String>,
        MovieQueryHandler.AsyncQueryListener {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String PAGE_SUFFIX = "&page=";
    private static final String PATH = "path";
    private static final int LOADER_ID_MOVIES_POPULAR = 101;
    private static final int LOADER_ID_MOVIES_TOP_RATED = 102;
    private static final int LOADER_ID_MOVIES_FAVOURITES = 103;
    private static final int LOADER_ID_TRAILERS = 200;
    private static final int LOADER_ID_REVIEWS = 300;
    private final MovieRepoListener mMovieListener;
    private TrailerReviewRepoListener mTrailerReviewListener;
    private final Context mContext;
    private int mTotalPages;
    private int mCurrentPage;
    private String mLastOrder;
    private final LoaderManager mLoaderManager;
    private int mCurrentLoaderId;

    public MovieRepositoryImpl(MovieRepoListener listener, Context ctx, LoaderManager loaderManager) {
        this.mMovieListener = listener;
        this.mContext = ctx;
        this.mLoaderManager = loaderManager;
        this.mCurrentPage = 0;
        this.mTotalPages = -1;
    }

    public MovieRepositoryImpl(MovieRepoListener movieListener, TrailerReviewRepoListener trailerListener, Context ctx, LoaderManager loaderManager) {
        this(movieListener, ctx, loaderManager);
        this.mTrailerReviewListener = trailerListener;
    }

    @Override
    public void getMovies(String order) {
        int loaderId = this.getLoaderId(order);
        if(this.mLastOrder == null) {
            this.mLastOrder = order;
        }
        if(!this.mLastOrder.equals(order)) {
            this.mCurrentPage = 0;
            this.mLastOrder = order;
        }
        if(this.mTotalPages != -1) {
            if(this.mCurrentPage >= this.mTotalPages || this.mCurrentPage < 0) {
                return;
            }
        }
        Bundle args = new Bundle();
        String path = StringUtil.formatPath(
                mContext.getString(R.string.url_format_movies),
                order, API_KEY) + PAGE_SUFFIX + ++this.mCurrentPage;
        args.putString(PATH, path);
        this.mCurrentLoaderId = loaderId;
        if(this.hasNetworkConnectivity()) {
            this.mLoaderManager.restartLoader(loaderId, args, this);
        } else {
            this.mMovieListener.onMoviesFailure(mContext.getString(R.string.no_internet_connection));
        }
    }

    @Override
    public void fetchNewPage(String order, int requestedPage) {
        this.mCurrentLoaderId = this.getLoaderId(order);
        Bundle args = new Bundle();
        String path = StringUtil.formatPath(
                mContext.getString(R.string.url_format_movies),
                order, API_KEY) + PAGE_SUFFIX + requestedPage;
        args.putString(PATH, path);
        if(this.hasNetworkConnectivity()) {
            this.mLoaderManager.restartLoader(this.mCurrentLoaderId, args, this);
        }
    }

    @Override
    public void getTrailers(long movieId) {
        String path = StringUtil.formatPath(mContext.getString(R.string.url_format_trailers), movieId, API_KEY);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        this.mLoaderManager.initLoader(LOADER_ID_TRAILERS, args, this);
    }

    @Override
    public void getReviews(long movieId) {
        String path = StringUtil.formatPath(mContext.getString(R.string.url_format_reviews), movieId, API_KEY);
        Bundle args = new Bundle();
        args.putString(PATH, path);
        this.mLoaderManager.initLoader(LOADER_ID_REVIEWS, args, this);
    }

    @Override
    public boolean isFavourite(Movie movie) {
        Cursor cursor = this.mContext.getContentResolver()
                .query(MovieDBEntry.getContentUriSingleRow(movie.getId()),
                        null, null, null, null);
        Movie result = null;
        if(cursor != null && cursor.moveToFirst()) {
            result = this.getOneRowFromCursor(cursor);
            cursor.close();
        }
        return result != null;
    }

    @Override
    public void getFavourites(String order) {
        this.mLastOrder = order;
        this.mCurrentPage = 0;
        this.mLoaderManager.initLoader(LOADER_ID_MOVIES_FAVOURITES, null, getFavouriteMovies);
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
        AsyncQueryHandler handler = new MovieQueryHandler(this.mContext.getContentResolver(), this);
        handler.startInsert(0, movie, MovieDBEntry.CONTENT_URI, cv);
    }

    @Override
    public void removeFromFavourite(Movie movie) {
        AsyncQueryHandler handler = new MovieQueryHandler(this.mContext.getContentResolver(), this);
        handler.startDelete(0, movie, MovieDBEntry.getContentUriSingleRow(movie.getId()),
                null, null);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        String path = bundle.getString(PATH);
        return new MovieLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        int loaderId = loader.getId();
        if(data == null) {
            this.mMovieListener.onMoviesFailure(this.mContext.getString(R.string.main_error_message));
            return;
        }
        try {
            switch (loaderId) {
                case LOADER_ID_MOVIES_POPULAR:
                case LOADER_ID_MOVIES_TOP_RATED:
                case LOADER_ID_MOVIES_FAVOURITES:
                    this.mTotalPages = JsonUtils.getTotalPages(data, this.mContext);
                    List<Movie> movies = JsonUtils.convertToMovieListObject(data, this.mContext);
                    this.mMovieListener.onMoviesSuccess(movies);
                    break;
                case LOADER_ID_TRAILERS:
                    List<Trailer> trailers = JsonUtils.convertToTrailerYoutubePaths(data, this.mContext);
                    this.mTrailerReviewListener.onTrailersSuccess(trailers);
                    break;
                case LOADER_ID_REVIEWS:
                    List<Review> reviews = JsonUtils.convertToReviews(data, this.mContext);
                    this.mTrailerReviewListener.onReviewsSuccess(reviews);
                    break;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            this.mMovieListener.onMoviesFailure(this.mContext.getString(R.string.main_error_message));
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onQueryCompleted(Object object) {
        if(object != null) {
            if(object instanceof Movie) {
                //insert, delete
                this.mMovieListener.onMoviesSuccess(Collections.singletonList((Movie) object));
                return;
            }
            if(object instanceof Cursor) {
                // query for one row
                Cursor cursor = (Cursor) object;
                Movie movie = null;
                if(cursor.moveToFirst()) {
                    movie = this.getOneRowFromCursor(cursor);
                    cursor.close();
                }
                this.mMovieListener.onMoviesSuccess(Collections.singletonList(movie));
            }
        }
        this.mMovieListener.onMoviesFailure("");
    }

    private LoaderManager.LoaderCallbacks<Cursor> getFavouriteMovies
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(mContext, MovieDBEntry.CONTENT_URI,
                    null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
            List<Movie> movies = new ArrayList<>();
            while(c.moveToNext()) {
                movies.add(getOneRowFromCursor(c));
            }
            mMovieListener.onMoviesSuccess(movies);
            mLoaderManager.destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private int getLoaderId(String order) {
        int loaderId = LOADER_ID_MOVIES_POPULAR;
        if(order.equals(this.mContext.getString(R.string.top_rated_order))) {
            loaderId = LOADER_ID_MOVIES_TOP_RATED;
        } else if(order.equals(this.mContext.getString(R.string.favourites_order))) {
            loaderId = LOADER_ID_MOVIES_FAVOURITES;
        }
        return loaderId;
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

    private boolean hasNetworkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}