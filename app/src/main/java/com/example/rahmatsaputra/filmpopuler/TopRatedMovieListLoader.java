package com.example.rahmatsaputra.filmpopuler;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.rahmatsaputra.filmpopuler.data.api.TmdbService;
import com.example.rahmatsaputra.filmpopuler.data.db.table.Movie;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieDataResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by RahmatSaputra on 01/11/2017.
 */

public class TopRatedMovieListLoader extends AsyncTaskLoader<List<MovieData>> {

    private static final String TAG = "TopRatedMovieListLoader";
    public static final int TOPRATED_MOVIE_LIST_LOADER_ID = 601;
    List<MovieData> movieDataList;

    public TopRatedMovieListLoader(Context context){
        super(context);
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
        MovieDataResponse data = new MovieDataResponse();
        try{
            result.addAll(data.getMovieDataList());
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
