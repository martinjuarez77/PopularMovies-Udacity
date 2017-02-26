package com.martincho.udacity.popularmovies.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by martincho on 23/2/17.
 */

public class PopularMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.martincho.udacity.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR_MOVIES = "popularmovies";


    public static final class PopularMovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR_MOVIES)
                .build();

        public static final String TABLE_NAME = "popularmovies";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_PLOT_SYNOPSIS = "plotSynopsis";


    }

}
