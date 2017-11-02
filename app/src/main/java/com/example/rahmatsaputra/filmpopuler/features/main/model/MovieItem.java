package com.example.rahmatsaputra.filmpopuler.features.main.model;

import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public interface MovieItem extends MainItem {
    String getMovieId();

    String getMovieTitle();

    String getPosterPath();

    MovieData getMovieData();
}
