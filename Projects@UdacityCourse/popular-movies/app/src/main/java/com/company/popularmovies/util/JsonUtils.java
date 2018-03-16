package com.company.popularmovies.util;

import android.content.Context;

import com.company.popularmovies.R;
import com.company.popularmovies.models.Movie;
import com.company.popularmovies.models.Review;
import com.company.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonUtils {

    private JsonUtils(){}

    public static int getTotalPages(String result, Context ctx) throws JSONException {
        return new JSONObject(result).getInt(ctx.getString(R.string.json_total_pages_key));
    }

    public static List<Movie> convertToMovieListObject(String result, Context ctx) throws JSONException, ParseException {
        List<Movie> movies = new ArrayList<>();
        JSONObject obj = new JSONObject(result);
        JSONArray jsonArray = obj.getJSONArray(ctx.getString(R.string.json_results_key));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currentMovie = jsonArray.getJSONObject(i);
            long id = currentMovie.getLong(ctx.getString(R.string.json_id_key));
            double rating = currentMovie.getDouble(ctx.getString(R.string.json_vote_key));
            String originalTitle = currentMovie.getString(ctx.getString(R.string.json_title_key));
            String thumbnailPath = currentMovie.getString(ctx.getString(R.string.json_poster_path_key));
            thumbnailPath = ctx.getString(R.string.pre_thumbnail_path) + thumbnailPath;
            String overview = currentMovie.getString(ctx.getString(R.string.json_overview_key));
            Date releaseDate = StringUtil.parseDate(currentMovie.getString(ctx.getString(R.string.json_release_date_key)));
            Movie movie = new Movie(id, originalTitle, thumbnailPath, null, overview, rating, releaseDate);
            movies.add(movie);
        }
        return movies;
    }

    public static List<Trailer> convertToTrailerYoutubePaths(String result, Context ctx) throws JSONException {
        List<Trailer> trailers = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(result).getJSONArray(ctx.getString(R.string.json_results_key));
        String basePath = ctx.getString(R.string.url_youtube_trailer_format);
        for (int i = 0; i < jsonArray.length(); i++) {
            String youtubeKey = jsonArray.getJSONObject(i).getString(ctx.getString(R.string.json_key));
            String name = jsonArray.getJSONObject(i).getString(ctx.getString(R.string.json_name_key));
            String youtubePath = String.format(basePath, youtubeKey);
            trailers.add(new Trailer(youtubeKey, youtubePath, name));
        }
        return trailers;
    }

    public static List<Review> convertToReviews(String result, Context ctx) throws JSONException {
        List<Review> reviews = new ArrayList<>();
        JSONArray jsonArray = new JSONObject(result).getJSONArray(ctx.getString(R.string.json_results_key));
        for (int i = 0; i < jsonArray.length(); i++) {
            String author = jsonArray.getJSONObject(i).getString(ctx.getString(R.string.json_author_key));
            String content = jsonArray.getJSONObject(i).getString(ctx.getString(R.string.json_content_key));
            reviews.add(new Review(author, content));
        }
        return reviews;

    }
}