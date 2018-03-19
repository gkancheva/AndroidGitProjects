package com.company.popularmovies.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final int TIMEOUT_SECONDS = 5000;
    private static final String REQUEST_METHOD_GET = "GET";

    public static String getHttpGETResponse(String path) throws IOException {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            prepareConnectionGETRequest(connection);

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                return getResponseInStringBuilder(connection);
            }
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static byte[] getBitmapFromURL(String path) throws IOException {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            return bos.toByteArray();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void prepareConnectionGETRequest(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod(REQUEST_METHOD_GET);
        connection.setReadTimeout(TIMEOUT_SECONDS);
        connection.setConnectTimeout(TIMEOUT_SECONDS);
        connection.connect();
    }

    private static String getResponseInStringBuilder(HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            break;
        }
        br.close();
        return sb.toString();
    }

}