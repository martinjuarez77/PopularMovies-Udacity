package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martincho.udacity.popularmovies.model.MovieBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by martincho on 28/1/17.
 */

public class MoviesAdapter  extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<MovieBean> moviesArray;

    private final MovieAdapterOnClickHandler onClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieBean movieBean);
    }

    public MoviesAdapter(MovieAdapterOnClickHandler _onClickHandler){
        onClickHandler = _onClickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieImageThumbnail;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            movieImageThumbnail = (ImageView) view.findViewById(R.id.movieImageThumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieBean movieBean = moviesArray.get(adapterPosition);
            onClickHandler.onClick(movieBean);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder movieAdapterViewHolder, int position) {

        Context context = movieAdapterViewHolder.movieImageThumbnail.getContext();

        MovieBean movieDetails = moviesArray.get(position);

        String IMAGE_PATH = context.getString(R.string.themoviedb_image_url);

        if (movieDetails.getMoviePoster()!= null ) {

            Picasso.with(context).load(IMAGE_PATH + movieDetails.getMoviePoster()).into(movieAdapterViewHolder.movieImageThumbnail);
        }

    }

    @Override
    public int getItemCount() {
        if (moviesArray == null){
            return 0;
        }
        return moviesArray.size();
    }

    public void setMoviesData(ArrayList<MovieBean> _moviesArray) {
        moviesArray = _moviesArray;
        notifyDataSetChanged();
    }
}
