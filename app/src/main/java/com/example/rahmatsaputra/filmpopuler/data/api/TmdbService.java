package com.example.rahmatsaputra.filmpopuler.data.api;

import android.support.annotation.NonNull;

import com.example.rahmatsaputra.filmpopuler.BuildConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rahmatsaputra.filmpopuler.data.api.TmdbConstant.API_BASE_URL;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public class TmdbService {
    private static final String BASE_URL = API_BASE_URL;
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    private static Gson gson;
    private static OkHttpClient client;

    private TmdbService() {
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    @NonNull
    private static OkHttpClient getHttpClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url().newBuilder()
                                    .addQueryParameter("api_key", API_KEY)
                                    .build();
                            request = request.newBuilder().url(url).build();

                            return chain.proceed(request);
                        }
                    }).build();
        }
        return client;
    }

    public static TmdbApiService open() {
        OkHttpClient client = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();

        return retrofit.create(TmdbApiService.class);
    }
}
