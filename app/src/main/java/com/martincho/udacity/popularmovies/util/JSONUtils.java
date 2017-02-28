package com.martincho.udacity.popularmovies.util;

import android.content.Context;

import com.martincho.udacity.popularmovies.MovieBean;
import com.martincho.udacity.popularmovies.MovieReviewBean;
import com.martincho.udacity.popularmovies.MovieTrailerBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by martincho on 28/1/17.
 */

public class JSONUtils {

    /**
     * JSON Transformation into an ArrayList
     * @param context
     * @param jsonString
     * @return
     * @throws JSONException
     */
    public static ArrayList<MovieBean> getMoviesArrayFromJson(Context context, String jsonString) throws JSONException {

        ArrayList<MovieBean> moviesArrayData = null;

        JSONObject moviesJson = new JSONObject(jsonString);

        if (!moviesJson.has("results")) {
            return null;
        }

        JSONArray moviesArray = moviesJson.getJSONArray("results");

        moviesArrayData = new ArrayList<MovieBean>();

        for (int i = 0; i < moviesArray.length(); i++) {

            MovieBean movie = new MovieBean();

            JSONObject movieDetails = moviesArray.getJSONObject(i);

            movie.setTitle(movieDetails.getString("title"));
            movie.setReleaseDate(movieDetails.getString("release_date"));
            movie.setMoviePoster(movieDetails.getString("poster_path"));
            movie.setPlotSynopsis(movieDetails.getString("overview"));
            movie.setVoteAverage(movieDetails.getString("vote_average"));
            movie.setId(movieDetails.getString("id"));

            moviesArrayData.add(movie);
        }

        return moviesArrayData;
    }

    public static ArrayList<MovieReviewBean> getReviewsArrayFromJson(Context context, String jsonString) throws JSONException {

        ArrayList<MovieReviewBean> reviewsArrayData = null;

        JSONObject reviewsJson = new JSONObject(jsonString);

        if (!reviewsJson.has("results")) {
            return null;
        }

        JSONArray moviesArray = reviewsJson.getJSONArray("results");

        reviewsArrayData = new ArrayList<MovieReviewBean>();

        for (int i = 0; i < moviesArray.length(); i++) {

            MovieReviewBean review = new MovieReviewBean();

            JSONObject reviewDetails = moviesArray.getJSONObject(i);
            review.setAuthor(reviewDetails.getString("author"));
            review.setContent(reviewDetails.getString("content"));

            reviewsArrayData.add(review);
        }

        return reviewsArrayData;
    }


    public static ArrayList<MovieTrailerBean> getTrailersArrayFromJson(Context context, String jsonString) throws JSONException {

        ArrayList<MovieTrailerBean> trailersArrayData = null;

        JSONObject trailersJson = new JSONObject(jsonString);

        if (!trailersJson.has("youtube")) {
            return null;
        }

        JSONArray trailersArray = trailersJson.getJSONArray("youtube");

        trailersArrayData = new ArrayList<MovieTrailerBean>();

        for (int i = 0; i < trailersArray.length(); i++) {

            MovieTrailerBean trailer = new MovieTrailerBean();

            JSONObject trailersDetails = trailersArray.getJSONObject(i);

            trailer.setName(trailersDetails.getString("name"));
            trailer.setSize(trailersDetails.getString("size"));
            trailer.setSource(trailersDetails.getString("source"));
            trailer.setType(trailersDetails.getString("type"));

            trailersArrayData.add(trailer);
        }

        return trailersArrayData;
    }


}
