package com.company.popularmovies.repository;

import android.content.Context;

import com.company.popularmovies.BuildConfig;
import com.company.popularmovies.R;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.services.AsyncTaskListener;
import com.company.popularmovies.services.GetMoviesAsyncTask;
import com.company.popularmovies.services.MovieRepoListener;
import com.company.popularmovies.util.JsonUtils;
import com.company.popularmovies.util.StringUtil;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository, AsyncTaskListener {

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String PAGE_SUFFIX = "&page=";
    private MovieRepoListener mListener;
    private Context mContext;
    private int mTotalPages;
    private int mCurrentPage;
    private String mCurrentOrder;

    public MovieRepositoryImpl(MovieRepoListener listener, Context ctx) {
        this.mListener = listener;
        this.mContext = ctx;
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
        this.executeTask(path);
    }

    @Override
    public void onBackgroundSuccess(String result) {
        if(result == null) {
            this.mListener.onMoviesFailure();
            return;
        }
        try {
            this.mTotalPages = JsonUtils.getTotalPages(result, this.mContext);
            List<Movie> movies = JsonUtils.convertToMovieListObject(result, this.mContext);
            this.mListener.onMoviesSuccess(movies);
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.mListener.onMoviesFailure();
    }

    private void executeTask(String path) {
        new GetMoviesAsyncTask(this.mContext, this).execute(path);
    }
}