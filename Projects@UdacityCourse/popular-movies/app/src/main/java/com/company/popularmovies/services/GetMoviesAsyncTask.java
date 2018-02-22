package com.company.popularmovies.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.company.popularmovies.util.NetworkUtils;

import java.io.IOException;

public class GetMoviesAsyncTask extends AsyncTask<String, Void, String> {

    private AsyncTaskListener mListener;
    private Context mContext;

    public GetMoviesAsyncTask(Context ctx, AsyncTaskListener listener) {
        this.mListener = listener;
        this.mContext = ctx;
    }

    @Override
    protected String doInBackground(String... paths) {
        try {
            if(this.hasNetworkConnectivity()) {
                return NetworkUtils.getHttpGETResponse(paths[0]);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        this.mListener.onBackgroundSuccess(result);
    }

    private boolean hasNetworkConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager)this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}