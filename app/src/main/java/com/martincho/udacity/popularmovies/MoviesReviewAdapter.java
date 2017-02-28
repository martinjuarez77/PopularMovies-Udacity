package com.martincho.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by martincho on 28/1/17.
 */

public class MoviesReviewAdapter extends RecyclerView.Adapter<MoviesReviewAdapter.MoviesReviewAdapterViewHolder> {

    private ArrayList<MovieReviewBean> moviesReviewArray;

    private final MovieReviewAdapterOnClickHandler onClickHandler;

    public interface MovieReviewAdapterOnClickHandler {
        void onClick(MovieReviewBean movieReviewBean);
    }

    public MoviesReviewAdapter(MovieReviewAdapterOnClickHandler _onClickHandler){
        onClickHandler = _onClickHandler;
    }

    public class MoviesReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView reviewAuthor;
        public final TextView reviewContent;

        public MoviesReviewAdapterViewHolder(View view) {
            super(view);
            reviewAuthor = (TextView) view.findViewById(R.id.review_author);
            reviewContent = (TextView) view.findViewById(R.id.review_content);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieReviewBean movieReviewBean = moviesReviewArray.get(adapterPosition);
            onClickHandler.onClick(movieReviewBean);
        }
    }

    @Override
    public MoviesReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MoviesReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesReviewAdapterViewHolder movieReviewAdapterViewHolder, int position) {

        MovieReviewBean movieReviewDetails = moviesReviewArray.get(position);
        movieReviewAdapterViewHolder.reviewAuthor.setText(movieReviewDetails.getAuthor());
        movieReviewAdapterViewHolder.reviewContent.setText(movieReviewDetails.getContent());

    }

    @Override
    public int getItemCount() {
        if (moviesReviewArray == null){
            return 0;
        }
        return moviesReviewArray.size();
    }

    public void setMoviesReviewData(ArrayList<MovieReviewBean> _moviesReviewArray) {
        moviesReviewArray = _moviesReviewArray;
        notifyDataSetChanged();
    }
}
