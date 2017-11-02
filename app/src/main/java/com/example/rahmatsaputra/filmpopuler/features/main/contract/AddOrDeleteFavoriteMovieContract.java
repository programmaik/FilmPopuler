package com.example.rahmatsaputra.filmpopuler.features.main.contract;

/**
 * Created by RahmatSaputra on 31/10/2017.
 */

public final class AddOrDeleteFavoriteMovieContract {
    public static final String ACTION_FAVORITE_MOVIE_DATA_CHANGED = "com.example.rahmatsaputra.filmpopuler.features.main.contract.ACTION_FAVORITE_MOVIE_DATA_CHANGED";

    public static final String ACTION_ADD_FAVORITE = "com.example.rahmatsaputra.filmpopuler.features.main.contract.ACTION_ADD_FAVORITE";
    public static final String ACTION_DELETE_FAVORITE = "com.example.rahmatsaputra.filmpopuler.features.main.contract.ACTION_DELETE_FAVORITE";

    public static final String EXTRA_MOVIE_DATA = "EXTRA_MOVIE_DATA";
    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";

    private AddOrDeleteFavoriteMovieContract() {
    }
}
