package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martincho.udacity.popularmovies.model.MovieTrailerBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by martincho on 28/1/17.
 */

public class MoviesTrailerAdapter extends RecyclerView.Adapter<MoviesTrailerAdapter.MoviesTrailerAdapterViewHolder> {

    private ArrayList<MovieTrailerBean> moviesTrailerArray;

    private final MovieTrailerAdapterOnClickHandler onClickHandler;

    public interface MovieTrailerAdapterOnClickHandler {
        void onClick(MovieTrailerBean movieTrailerBean);
    }

    public MoviesTrailerAdapter(MovieTrailerAdapterOnClickHandler _onClickHandler){
        onClickHandler = _onClickHandler;
    }

    public class MoviesTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieTrailerThumbnail;

        public MoviesTrailerAdapterViewHolder(View view) {
            super(view);
            movieTrailerThumbnail = (ImageView) view.findViewById(R.id.movie_trailer_thumbnail);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieTrailerBean movieTrailerBean = moviesTrailerArray.get(adapterPosition);
            onClickHandler.onClick(movieTrailerBean);
        }
    }

    @Override
    public MoviesTrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesTrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesTrailerAdapterViewHolder movieTrailerAdapterViewHolder, int position) {

        Context context = movieTrailerAdapterViewHolder.movieTrailerThumbnail.getContext();

        MovieTrailerBean movieTrailerDetails = moviesTrailerArray.get(position);


        String IMAGE_TRAILER_PATH = context.getString(R.string.youtube_image_trailer_url);

        if (movieTrailerDetails.getSource() != null ) {

            Picasso.with(context).load(IMAGE_TRAILER_PATH.replaceAll("#", movieTrailerDetails.getSource()) ).into(movieTrailerAdapterViewHolder.movieTrailerThumbnail);
        }


    }

    @Override
    public int getItemCount() {
        if (moviesTrailerArray == null){
            return 0;
        }
        return moviesTrailerArray.size();
    }

    public void setMoviesTrailerData(ArrayList<MovieTrailerBean> _moviesTrailerArray) {
        moviesTrailerArray = _moviesTrailerArray;
        notifyDataSetChanged();
    }
}
