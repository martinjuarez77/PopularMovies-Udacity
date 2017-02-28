package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.martincho.udacity.popularmovies.util.JSONUtils;
import com.martincho.udacity.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity
        implements MoviesReviewAdapter.MovieReviewAdapterOnClickHandler, MoviesTrailerAdapter.MovieTrailerAdapterOnClickHandler {



    TextView movie_title;
    TextView movie_release_date;
    ImageView movie_movie_poster;
    TextView movie_vote_average;
    TextView movie_plot_synopsis;

    private RecyclerView moviesReviewsRecyclerView;
    private RecyclerView moviesTrailersRecyclerView;

    private MoviesReviewAdapter movieReviewsAdapter;
    private MoviesTrailerAdapter movieTrailersAdapter;

    private TextView errorMessage;

    private ProgressBar progressBar;

    private String THEMOVIEDB_REVIEWS_URL;
    private String THEMOVIEDB_TRAILERS_URL;
    private String THEMOVIEDB_URL_CHECK_CONNECTION;
    private String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String IMAGE_PATH = getString(R.string.themoviedb_image_details_url);

        Intent previousIntent = getIntent();

        final MovieBean movieBean = previousIntent.getParcelableExtra("movieBean");

        String title = movieBean.getTitle();
        String releaseDate = movieBean.getReleaseDate();
        String moviePoster = movieBean.getMoviePoster();
        String voteAverage = movieBean.getVoteAverage();
        String plotSynopsis = movieBean.getPlotSynopsis();

        movie_title = (TextView) findViewById(R.id.movie_title);
        movie_release_date = (TextView) findViewById(R.id.movie_release_date);
        movie_movie_poster = (ImageView) findViewById(R.id.movie_movie_poster);
        movie_vote_average = (TextView) findViewById(R.id.movie_vote_average);
        movie_plot_synopsis = (TextView) findViewById(R.id.movie_plot_synopsis);

        movie_title.setText(title);
        movie_release_date.setText(releaseDate);

        movie_vote_average.setText(voteAverage);
        movie_plot_synopsis.setText(plotSynopsis);

        if (movieBean.getMoviePoster()!= null ) {
            Picasso.with(this).load(IMAGE_PATH + movieBean.getMoviePoster()).into(movie_movie_poster);
        }

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.addFavorite);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addFavoriteMovie = new Intent(DetailsActivity.this, AddFavoriteMovieActivity.class);
                addFavoriteMovie.putExtra("movieBean", movieBean);
                startActivity(addFavoriteMovie);
            }
        });


        errorMessage = (TextView) findViewById(R.id.errorMessage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        moviesReviewsRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_reviews_movie);
        RecyclerView.LayoutManager layoutReviewManager = new GridLayoutManager(this, 1);
        moviesReviewsRecyclerView.setLayoutManager(layoutReviewManager);
        moviesReviewsRecyclerView.setHasFixedSize(true);
        movieReviewsAdapter = new MoviesReviewAdapter(this);
        moviesReviewsRecyclerView.setAdapter(movieReviewsAdapter);

        moviesTrailersRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_trailers_movie);
        GridLayoutManager layoutTrailerManager = new GridLayoutManager(this, 2);
        moviesTrailersRecyclerView.setLayoutManager(layoutTrailerManager);
        moviesTrailersRecyclerView.setHasFixedSize(true);
        movieTrailersAdapter = new MoviesTrailerAdapter(this);
        moviesTrailersRecyclerView.setAdapter(movieTrailersAdapter);


        loadVariables();

        loadReviewsData(movieBean.getId());
        loadTrailersData(movieBean.getId());

    }

    private void loadVariables() {

        THEMOVIEDB_REVIEWS_URL = getString(R.string.themoviedb_reviews_url);
        THEMOVIEDB_TRAILERS_URL = getString(R.string.themoviedb_trailers_url);
        THEMOVIEDB_URL_CHECK_CONNECTION = getString(R.string.themoviedb_url_check_connection);
        API_KEY = getString(R.string.api_key);
    }
    /**
     * Show movie's reviews
     */
    private void showMovieReviewDataView() {
        //errorMessage.setVisibility(View.INVISIBLE);
        moviesReviewsRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show movie's reviews error message
     */
    private void showMovieReviewErrorMessage() {
        moviesReviewsRecyclerView.setVisibility(View.INVISIBLE);
        //errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Show movie's trailers
     */
    private void showMovieTrailersDataView() {
        //errorMessage.setVisibility(View.INVISIBLE);
        moviesTrailersRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show movie's trailers error message
     */
    private void showMovieTrailersErrorMessage() {
        moviesTrailersRecyclerView.setVisibility(View.INVISIBLE);
        //errorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * Load reviews movie list passing id
     * @param query
     */
    private void loadReviewsData(String query) {

        showMovieReviewDataView();

        new FetchReviewsTask().execute(query, THEMOVIEDB_REVIEWS_URL, THEMOVIEDB_URL_CHECK_CONNECTION);
    }

    /**
     * Load reviews movie list passing id
     * @param query
     */
    private void loadTrailersData(String query) {

        showMovieTrailersDataView();

        new FetchTrailersTask().execute(query, THEMOVIEDB_TRAILERS_URL, THEMOVIEDB_URL_CHECK_CONNECTION);
    }


    /**
     * Inner class to fetch data in async mode
     */
    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<MovieReviewBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieReviewBean> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String query = params[0];
            URL requestUrl = NetworkUtils.buildUrlWithMovieId(THEMOVIEDB_REVIEWS_URL, query, API_KEY);

            if (NetworkUtils.checkInternetAccess(THEMOVIEDB_URL_CHECK_CONNECTION, DetailsActivity.this)){
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                    ArrayList<MovieReviewBean> reviewsArrayData = JSONUtils.getReviewsArrayFromJson(DetailsActivity.this, jsonResponse);

                    return reviewsArrayData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieReviewBean> reviewsData) {
            progressBar.setVisibility(View.INVISIBLE);
            if (reviewsData != null) {
                showMovieReviewDataView();
                movieReviewsAdapter.setMoviesReviewData(reviewsData);
            } else {
                showMovieReviewErrorMessage();
            }
        }

    }

    /**
     * Inner class to fetch data in async mode
     */
    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<MovieTrailerBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieTrailerBean> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String query = params[0];
            URL requestUrl = NetworkUtils.buildUrlWithMovieId(THEMOVIEDB_TRAILERS_URL, query, API_KEY);

            if (NetworkUtils.checkInternetAccess(THEMOVIEDB_URL_CHECK_CONNECTION, DetailsActivity.this)){
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestUrl);

                    ArrayList<MovieTrailerBean> trailersArrayData = JSONUtils.getTrailersArrayFromJson(DetailsActivity.this, jsonResponse);

                    return trailersArrayData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<MovieTrailerBean> trailersData) {
            progressBar.setVisibility(View.INVISIBLE);
            if (trailersData != null) {
                showMovieTrailersDataView();
                movieTrailersAdapter.setMoviesTrailerData(trailersData);
            } else {
                showMovieTrailersErrorMessage();
            }
        }

    }
    /**
     * onClick implementations, passing movie data
     * @param movieReviewBean
     */
    @Override
    public void onClick(MovieReviewBean movieReviewBean) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);

        intentToStartDetailActivity.putExtra("movieReviewBean", movieReviewBean);

        startActivity(intentToStartDetailActivity);
    }

    /**
     * onClick implementations, passing movie data
     * @param movieTrailerBean
     */
    @Override
    public void onClick(MovieTrailerBean movieTrailerBean) {

        String moviewId = movieTrailerBean.getSource();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + moviewId));

            intent.putExtra("VIDEO_ID", moviewId);

            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e){

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_view_trailer_url) + moviewId));

            startActivity(intent);
        }
    }

}
