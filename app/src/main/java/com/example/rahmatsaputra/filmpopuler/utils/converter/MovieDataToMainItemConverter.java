package com.example.rahmatsaputra.filmpopuler.utils.converter;

import android.support.annotation.NonNull;

import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;
import com.example.rahmatsaputra.filmpopuler.features.main.model.BigMovieItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.HeaderItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MainItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MovieItemBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public class MovieDataToMainItemConverter {
    private MovieDataToMainItemConverter() {
    }

    public static List<MainItem> getMainItemList(String headerTitle, @NonNull List<MovieData> movieDataList) {
        List<MainItem> results = new ArrayList<>();
        int pos = 0;
        results.add(new HeaderItem(headerTitle));
        for (MovieData movieData : movieDataList) {
            results.add(itemCreator(movieData, pos));
            pos++;
        }
        return results;
    }

    static MainItem itemCreator(MovieData movieData, int position) {
        if (position % 5 == 0) {
            return new BigMovieItem(
                    String.valueOf(movieData.getId()),
                    movieData.getTitle(),
                    movieData.getPosterPath(),
                    movieData.getOverview(),
                    movieData);
        } else {
            return new MovieItemBuilder(
                    String.valueOf(movieData.getId()),
                    movieData.getTitle(),
                    movieData.getPosterPath(),
                    movieData);
        }
    }
}
