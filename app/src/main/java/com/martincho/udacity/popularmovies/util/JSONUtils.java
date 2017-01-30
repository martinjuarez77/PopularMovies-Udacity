package com.martincho.udacity.popularmovies.util;

import android.content.Context;

import com.martincho.udacity.popularmovies.MovieBean;

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

            moviesArrayData.add(movie);
        }

        return moviesArrayData;
    }



}
