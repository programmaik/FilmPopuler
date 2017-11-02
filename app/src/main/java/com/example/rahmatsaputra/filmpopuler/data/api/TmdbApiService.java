package com.example.rahmatsaputra.filmpopuler.data.api;

import com.example.rahmatsaputra.filmpopuler.data.model.MovieDataResponse;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public interface TmdbApiService {
    @GET("movie/popular")
    Call<MovieDataResponse> getMostPopularMovies(@Query("page") int page);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") String movieId);

    @GET("movie/top_rated")
    Call<MovieDataResponse> getTopRatedMovies(@Query("page") int page);

}
