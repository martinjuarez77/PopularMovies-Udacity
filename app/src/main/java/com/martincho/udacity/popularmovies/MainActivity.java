package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martincho.udacity.popularmovies.db.PopularMoviesContract;
import com.martincho.udacity.popularmovies.model.MovieBean;
import com.martincho.udacity.popularmovies.util.JSONUtils;
import com.martincho.udacity.popularmovies.util.NetworkUtils;
import com.martincho.udacity.popularmovies.util.PreferencesUtil;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements
        MoviesAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<ArrayList<MovieBean>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int LOADER_ID = 0;

    private String MOST_RATED = "";
    private String MOST_POPULAR = "";
    private String MY_FAVORITES = "";
    private String THEMOVIEDB_URL;
    private String THEMOVIEDB_URL_CHECK_CONNECTION;
    private String API_KEY;

    private RecyclerView moviesRecyclerView;

    private MoviesAdapter moviesAdapter;

    private TextView errorMessage;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadVariables();

        setContentView(R.layout.activity_main);

        moviesRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        errorMessage = (TextView) findViewById(R.id.errorMessage);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        moviesRecyclerView.setLayoutManager(layoutManager);

        moviesRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);

        moviesRecyclerView.setAdapter(moviesAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        int loaderId = LOADER_ID;

        LoaderManager.LoaderCallbacks<ArrayList<MovieBean>> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        //PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);


    }


    private void loadVariables() {

        MOST_RATED = getString(R.string.url_action_most_rated);
        MOST_POPULAR = getString(R.string.url_action_most_popular);
        MY_FAVORITES = getString(R.string.url_action_favorites);

        THEMOVIEDB_URL = getString(R.string.themoviedb_url);
        THEMOVIEDB_URL_CHECK_CONNECTION = getString(R.string.themoviedb_url_check_connection);
        API_KEY = getString(R.string.api_key);
    }

    /**
     * Show movies
     */
    private void showMovieDataView() {
        errorMessage.setVisibility(View.INVISIBLE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show error message
     */
    private void showErrorMessage() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * onClick implementations, passing movie data
     *
     * @param movieBean
     */
    @Override
    public void onClick(MovieBean movieBean) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra("movieBean", movieBean);

        startActivity(intentToStartDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    /**
     * Load data using sort method selected on menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_most_popular) {

            PreferencesUtil.changeQueryPreferences(MOST_POPULAR, this);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        } else if (id == R.id.action_most_rated) {

            PreferencesUtil.changeQueryPreferences(MOST_RATED, this);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        } else if (id == R.id.action_my_favorites) {

            PreferencesUtil.changeQueryPreferences(MY_FAVORITES, this);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public Loader<ArrayList<MovieBean>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieBean>>(this) {

            ArrayList<MovieBean> moviesData = null;

            /**
             * Subclasses of AsyncTaskLoader must implement this to take care of loading their data.
             */
            @Override
            protected void onStartLoading() {
                if (moviesData != null) {
                    deliverResult(moviesData);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieBean> loadInBackground() {

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getContext());

                String query = sp.getString("QUERY", getString(R.string.url_action_most_popular));

                // query favorite movies from content provider
                if (MY_FAVORITES.equals(query)) {

                    ArrayList<MovieBean> moviesArrayData = new ArrayList<MovieBean>();
                    try {
                        Cursor movieData = getContentResolver().query(PopularMoviesContract.PopularMovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                PopularMoviesContract.PopularMovieEntry.COLUMN_TITLE);

                        while (movieData.moveToNext()) {
                            MovieBean movieBean = new MovieBean();
                            movieBean.setTitle(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_TITLE)));
                            movieBean.setReleaseDate(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE)));
                            movieBean.setMoviePoster(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_POSTER)));
                            movieBean.setPlotSynopsis(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_PLOT_SYNOPSIS)));
                            movieBean.setVoteAverage(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE)));
                            movieBean.setId(movieData.getString(movieData.getColumnIndex(PopularMoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID)));
                            moviesArrayData.add(movieBean);

                        }
                        return moviesArrayData;


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                // query movies from themoviedb api
                else {
                    URL requestUrl = NetworkUtils.buildUrl(THEMOVIEDB_URL, query, API_KEY);

                    if (NetworkUtils.checkInternetAccess(THEMOVIEDB_URL_CHECK_CONNECTION, MainActivity.this)) {
                        try {
                            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                            ArrayList<MovieBean> moviesArrayData = JSONUtils.getMoviesArrayFromJson(MainActivity.this, jsonResponse);

                            return moviesArrayData;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            }

            /**
             * Sends the result of the load to the registered listener.
             *
             * @param _moviesData The result of the load
             */
            public void deliverResult(ArrayList<MovieBean> _moviesData) {
                moviesData = _moviesData;
                super.deliverResult(_moviesData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieBean>> loader, ArrayList<MovieBean> _moviesData) {
        progressBar.setVisibility(View.INVISIBLE);
        if (_moviesData != null) {
            showMovieDataView();
            moviesAdapter.setMoviesData(_moviesData);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieBean>> loader) {

    }


}
