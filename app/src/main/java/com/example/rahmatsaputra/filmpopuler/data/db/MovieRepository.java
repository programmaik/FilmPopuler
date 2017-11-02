package com.example.rahmatsaputra.filmpopuler.data.db;

import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;

import java.util.List;

/**
 * Created by RahmatSaputra on 24/10/2017.
 */

public interface MovieRepository {
    List<MovieData> getFavoriteMovie();

    void addFavoriteMovie(MovieData movieData);

    boolean isMovieFavored(String movieId);

    void updateFavoriteMovie(MovieData movieData);

    void removeFavoriteMovie(String movieId);
}
