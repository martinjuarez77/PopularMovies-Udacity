package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martincho.udacity.popularmovies.util.JSONUtils;
import com.martincho.udacity.popularmovies.util.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

    private String MOST_POPULAR;
    private String MOST_RATED;
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

        // load data from themoviedb service
        loadMoviesData(null);

    }

    /**
     * Load movie list passing query
     * @param query
     */
    private void loadMoviesData(String query) {

        showMovieDataView();

        // default query
        if (query == null || "".equals(query)){
            query = MOST_POPULAR;
        }

        new FetchMoviesTask().execute(query, THEMOVIEDB_URL, THEMOVIEDB_URL_CHECK_CONNECTION);
    }

    private void loadVariables(){
        MOST_POPULAR = getString(R.string.url_action_most_popular);
        MOST_RATED = getString(R.string.url_action_most_rated);
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
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_most_popular) {
            loadMoviesData(MOST_POPULAR);
            return true;
        } else if (id == R.id.action_most_rated) {
            loadMoviesData(MOST_RATED);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Inner class to fetch data in async mode
     */
    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieBean> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String query = params[0];
            URL requestUrl = NetworkUtils.buildUrl(THEMOVIEDB_URL, query, API_KEY);

            if (NetworkUtils.checkInternetAccess(THEMOVIEDB_URL_CHECK_CONNECTION, MainActivity.this)){
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

        @Override
        protected void onPostExecute(ArrayList<MovieBean> moviesData) {
            progressBar.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMovieDataView();
                moviesAdapter.setMoviesData(moviesData);
            } else {
                showErrorMessage();
            }
        }

    }
}
