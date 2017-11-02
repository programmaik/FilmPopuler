package com.example.rahmatsaputra.filmpopuler.features.main.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.rahmatsaputra.filmpopuler.data.db.DatabaseHelper;
import com.example.rahmatsaputra.filmpopuler.data.db.MovieRepository;
import com.example.rahmatsaputra.filmpopuler.data.db.table.Movie;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;

import static com.example.rahmatsaputra.filmpopuler.features.main.contract.AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE;
import static com.example.rahmatsaputra.filmpopuler.features.main.contract.AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE;
import static com.example.rahmatsaputra.filmpopuler.features.main.contract.AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_DATA;
import static com.example.rahmatsaputra.filmpopuler.features.main.contract.AddOrDeleteFavoriteMovieContract.EXTRA_MOVIE_ID;

/**
 * Created by RahmatSaputra on 31/10/2017.
 */

public final class AddOrDeleteFavoriteMovieService extends IntentService{

    private MovieRepository movieRepository;
    private static final String TAG = "AddOrDeleteFavoriteMovi";

    public AddOrDeleteFavoriteMovieService(){
        super(TAG);
    }

    public AddOrDeleteFavoriteMovieService(String name){
        super(name);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        movieRepository = DatabaseHelper.getInstance(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            Log.d(TAG, "onHandleIntent: " + action);
            if(ACTION_ADD_FAVORITE.equals(action)){
                MovieData movieData = intent.getParcelableExtra(EXTRA_MOVIE_DATA);
                if(movieData != null){
                    movieRepository.addFavoriteMovie(movieData);
                }
            } else if (ACTION_DELETE_FAVORITE.equals(action)){
                String movieId = intent.getStringExtra(EXTRA_MOVIE_ID);
                if (!TextUtils.isEmpty(movieId)){
                    movieRepository.removeFavoriteMovie(movieId);
                }
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
        }
    }

}
