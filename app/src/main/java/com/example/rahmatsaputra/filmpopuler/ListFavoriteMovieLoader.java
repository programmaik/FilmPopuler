package com.example.rahmatsaputra.filmpopuler;

import android.content.Context;
import android.util.Log;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rahmatsaputra.filmpopuler.data.db.DatabaseHelper;
import com.example.rahmatsaputra.filmpopuler.data.db.MovieRepository;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RahmatSaputra on 26/10/2017.
 */

public class ListFavoriteMovieLoader extends AsyncTaskLoader<List<MovieData>> {

    private static final String TAG = "ListFavoriteMovieLoader";
    public static final int FAVORITE_MOVIE_LIST_LOADER_ID = 501;
    MovieRepository movieRepository;
    int favored;
    List<MovieData> movieDataList;

    public ListFavoriteMovieLoader(Context context) {
        super(context);
        movieRepository = DatabaseHelper.getInstance(context);
    }

    @Override
    public void onStartLoading(){
        super.onStartLoading();
        if (movieDataList != null) {
            deliverResult(movieDataList);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<MovieData> loadInBackground() {
        List<MovieData> result = new ArrayList<>();
        try{
            result.addAll(movieRepository.getFavoriteMovie());
        } catch (Exception e){
            Log.e (TAG, "loadInBackground", e);
        }
        return result;

    }
    @Override
    public void deliverResult(List<MovieData> data) {
        movieDataList = data;
        super.deliverResult(data);
    }
}
