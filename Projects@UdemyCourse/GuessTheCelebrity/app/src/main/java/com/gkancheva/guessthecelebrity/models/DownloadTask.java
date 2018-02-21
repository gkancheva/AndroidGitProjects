package com.gkancheva.guessthecelebrity.models;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... args) {
        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(args[0]);
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            while(data != -1) {
                result.append((char) data);
                data = reader.read();
            }
            return result.toString();
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return null;
    }
}
