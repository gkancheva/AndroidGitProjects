package com.company.popularmovies.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.company.popularmovies.util.NetworkUtils;

import java.io.IOException;

public class MovieLoaderTask extends AsyncTaskLoader<String> {

    private Context mContext;
    private String mPath;
    private String mQueryResult;

    public MovieLoaderTask(Context ctx, String path) {
        super(ctx);
        this.mContext = ctx;
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
            if(this.hasNetworkConnectivity()) {
                return NetworkUtils.getHttpGETResponse(this.mPath);
            }
            return null;
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

    private boolean hasNetworkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}