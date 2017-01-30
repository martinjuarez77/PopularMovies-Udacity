package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {



    TextView movie_title;
    TextView movie_release_date;
    ImageView movie_movie_poster;
    TextView movie_vote_average;
    TextView movie_plot_synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String IMAGE_PATH = getString(R.string.themoviedb_image_details_url);

        Intent previousIntent = getIntent();

        MovieBean movieBean = (MovieBean) previousIntent.getSerializableExtra("movieBean");

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

    }
}
