package com.udacity.gradle.builditbigger;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private static MyApi mMyApiService = null;
    private Context mContext;
    private EndpointAsyncTaskListener mListener;

    public EndpointAsyncTask(EndpointAsyncTaskListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(mMyApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            mMyApiService = builder.build();
        }
        mContext = params[0].first;

        try {
            return mMyApiService.provideJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        this.mListener.onJokeSuccess(joke);
        Toast.makeText(mContext, joke, Toast.LENGTH_LONG).show();
    }
}