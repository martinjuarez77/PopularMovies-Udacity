package com.martincho.udacity.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martincho on 23/2/17.
 */

public class PopularMoviesDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "popular_movies.db";

    private static final int DATABASE_VERSION = 2;

    public PopularMoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOTIRE_MOVIES_TABLE =

                "CREATE TABLE " + PopularMoviesContract.PopularMovieEntry.TABLE_NAME + " (" +

                PopularMoviesContract.PopularMovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER  NOT NULL, " +
                PopularMoviesContract.PopularMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE       + " INTEGER NOT NULL, "                 +
                PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL, " +
                PopularMoviesContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " INTEGER NOT NULL,"                  +
                PopularMoviesContract.PopularMovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOTIRE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
