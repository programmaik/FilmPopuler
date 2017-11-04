package com.example.rahmatsaputra.filmpopuler.features.main.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.rahmatsaputra.filmpopuler.MainActivity;
import com.example.rahmatsaputra.filmpopuler.data.api.TmdbService;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieDataResponse;
import com.example.rahmatsaputra.filmpopuler.utils.converter.MovieDataToMainItemConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.rahmatsaputra.filmpopuler.features.main.contract.TopRatedMovieListContract.SHOW_MOVIE_LIST;
/**
 * Created by RahmatSaputra on 03/11/2017.
 */

public final class TopRatedMovieListService extends IntentService {

    private static final String TAG = "TopRateMovie";
    MainActivity mainActivity;

    public  TopRatedMovieListService(){
        super(TAG);
    }
    public TopRatedMovieListService(String name) {
        super(name);
    }
    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            Log.d(TAG, "onHandleIntent: " + action);
                    MovieDataResponse data = new MovieDataResponse();
                    if (SHOW_MOVIE_LIST.equals(action)) {
                        List<MovieData> movies = data.getMovieDataList();
                        if(data != null){
                            if (movies != null && !movies.isEmpty()) {
                                MovieDataToMainItemConverter.getMainItemList("Top Rated Movie", movies);
                            }
                        }
                    }
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
                }


        }


    }
