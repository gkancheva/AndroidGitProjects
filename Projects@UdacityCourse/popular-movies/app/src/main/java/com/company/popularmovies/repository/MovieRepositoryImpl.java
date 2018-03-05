package com.company.popularmovies.repository;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.company.popularmovies.BuildConfig;
import com.company.popularmovies.R;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.services.MovieLoaderTask;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.util.JsonUtils;
import com.company.popularmovies.util.StringUtil;

import org.json.JSONException;

import java.text.ParseException;
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
    public Loader<String> onCreateLoader(int i, Bundle bundle) {

        String path = bundle.getString(PATH);
        return new MovieLoaderTask(this.mContext, path);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data == null) {
            this.mListener.onMoviesFailure();
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

}