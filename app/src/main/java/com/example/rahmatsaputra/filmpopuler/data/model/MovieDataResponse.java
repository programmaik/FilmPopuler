package com.example.rahmatsaputra.filmpopuler.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by RahmatSaputra on 21/10/2017.
 */

public class MovieDataResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<MovieData> movieDataList;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("status_code")
    private int statusCode;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieData> getMovieDataList() {
        return movieDataList;
    }

    public void setMovieDataList(List<MovieData> movieDataList) {
        this.movieDataList = movieDataList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "MovieDataResponse{" +
                "page=" + page +
                ", movieDataList=" + movieDataList +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", statusMessage='" + statusMessage + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
