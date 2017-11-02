package com.example.rahmatsaputra.filmpopuler;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.support.v4.content.Loader;

import com.example.rahmatsaputra.filmpopuler.data.db.DatabaseHelper;
import com.example.rahmatsaputra.filmpopuler.data.db.MovieRepository;
import com.example.rahmatsaputra.filmpopuler.data.db.table.Movie;

/**
 * Created by RahmatSaputra on 26/10/2017.
 */

public class FavoriteMovieLoader extends AsyncTaskLoader<Boolean> {

    public static final int FAVORITE_MOVIE_LOADER_ID = 101;
    private static final String TAG = "FavoriteMovieLoader";
    private MovieRepository movieRepository;
    private Boolean isFavorite = null;
    private String movieId;
    private int favored;
    Movie movie = new Movie();

    public FavoriteMovieLoader(Context context, String movieId){
        super(context);
        movieRepository = DatabaseHelper.getInstance(context);
        this.movieId = movieId;
    }

    @Override
    public void onStartLoading(){
        super.onStartLoading();
        if (isFavorite == null){
            forceLoad();
        } else {
            deliverResult(isFavorite);
        }
    }
    @Override
    public Boolean loadInBackground() {
        try{
            isFavorite = movieRepository.isMovieFavored(movieId);
        } catch (Exception e){
            Log.e (TAG, "loadInBackground", e);
        }
        return isFavorite;
    }
}
