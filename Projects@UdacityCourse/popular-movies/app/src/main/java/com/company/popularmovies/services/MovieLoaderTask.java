package com.company.popularmovies.services;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.company.popularmovies.util.NetworkUtils;

import java.io.IOException;

public class MovieLoaderTask extends AsyncTaskLoader<String> {

    private final String mPath;
    private String mQueryResult;

    public MovieLoaderTask(Context ctx, String path) {
        super(ctx);
        this.mPath = path;
    }

    @Override
    protected void onStartLoading() {
        if(this.mQueryResult != null) {
            deliverResult(this.mQueryResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        try {
            return NetworkUtils.getHttpGETResponse(this.mPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(String data) {
        this.mQueryResult = data;
        super.deliverResult(data);
    }
}