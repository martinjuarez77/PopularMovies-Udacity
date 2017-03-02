package com.martincho.udacity.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.martincho.udacity.popularmovies.db.PopularMoviesContract;
import com.martincho.udacity.popularmovies.model.MovieBean;
import com.martincho.udacity.popularmovies.util.PreferencesUtil;

/**
 * Created by martincho on 26/2/17.
 */
public class AddFavoriteMovieActivity  extends AppCompatActivity {

    private String MY_FAVORITES = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent previousIntent = getIntent();

        MY_FAVORITES = getString(R.string.url_action_favorites);

        final MovieBean movieBean = previousIntent.getParcelableExtra("movieBean");

        ContentValues contentValues = new ContentValues();

        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_TITLE, movieBean.getTitle());
        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE, movieBean.getReleaseDate());
        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_POSTER, movieBean.getMoviePoster());
        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_PLOT_SYNOPSIS, movieBean.getPlotSynopsis());
        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, movieBean.getVoteAverage());
        contentValues.put(PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID, movieBean.getId());

        // query for existing movie in favorites
        String [] resultColumns = {
                PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID
        };
        Cursor cursor = getContentResolver().query(PopularMoviesContract.PopularMovieEntry.CONTENT_URI,
                resultColumns, PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID + "=" +movieBean.getId(), null, null);

        // delete insert
        boolean isFavorite = false;

        if (cursor.moveToFirst()) {
            do {
                isFavorite = true;

                String [] arguments = new String[1];
                arguments[0] = movieBean.getId();
                String deletionClause = PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?";
                getContentResolver().delete(PopularMoviesContract.PopularMovieEntry.CONTENT_URI, deletionClause, arguments);

            } while (cursor.moveToNext());
        }

        if (!isFavorite){
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(PopularMoviesContract.PopularMovieEntry.CONTENT_URI, contentValues);

            Toast.makeText(this, getString(R.string.movie_added_to_favorites), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, getString(R.string.movie_removed_from_favorites), Toast.LENGTH_LONG).show();
        }

        PreferencesUtil.changeQueryPreferences(MY_FAVORITES, this);

        // load favorite movies list
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }


}
