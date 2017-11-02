package com.example.rahmatsaputra.filmpopuler.features.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahmatsaputra.filmpopuler.R;
import com.example.rahmatsaputra.filmpopuler.data.api.TmdbConstant;
import com.example.rahmatsaputra.filmpopuler.features.main.contract.MovieItemClickListener;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MainItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MovieItem;
import com.squareup.picasso.Picasso;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public class MovieViewHolder extends BaseViewHolder {
    private static final String SMALL_POSTER_SIZE = "w342/";
    private static final String MEDIUM_POSTER_SIZE = "w500/";

    private ImageView posterImageView;
    private MovieItemClickListener listener;
    private String imageSize;

    public MovieViewHolder(View itemView, MovieItemClickListener itemListener) {
        super(itemView);
        this.listener = itemListener;

        setupViews(itemView);
    }

    private void setupViews(View itemView) {
        posterImageView = itemView.findViewById(R.id.movie_item_poster_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onMovieItemClicked(pos);
                }
            }
        });
    }

    private void setImageSize(int position) {
        imageSize = positionDivisibleByFive(position) ? MEDIUM_POSTER_SIZE : SMALL_POSTER_SIZE;
    }

    private boolean positionDivisibleByFive(int position) {
        return position != RecyclerView.NO_POSITION && (position % 5 == 0);
    }

    @Override
    public void bindView(MainItem item) {
        MovieItem movieItem = (MovieItem) item;
        setImageSize(getAdapterPosition());
        Picasso.with(itemView.getContext())
                .load(TmdbConstant.IMAGE_BASE_URL + imageSize + movieItem.getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(posterImageView);
    }
}
