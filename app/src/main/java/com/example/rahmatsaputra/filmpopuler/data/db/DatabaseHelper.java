package com.example.rahmatsaputra.filmpopuler.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rahmatsaputra.filmpopuler.data.db.table.Movie;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper implements MovieRepository{
    private static final String DB_NAME = "filmpopuler.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper instance;


    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if(instance == null){
           instance = new DatabaseHelper(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Movie.CREATE_TABLE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public List<MovieData> getFavoriteMovie(){
        List<MovieData> results = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = getReadableDatabase();
        try {
            cursor = db.query(Movie.TABLE_NAME,
                    new String[]{
                            Movie.COLUMN_MOVIE_ID,
                            Movie.COLUMN_MOVIE_TITLE,
                            Movie.COLUMN_MOVIE_OVERVIEW,
                            Movie.COLUMN_MOVIE_VOTE_COUNT,
                            Movie.COLUMN_MOVIE_VOTE_AVERAGE,
                            Movie.COLUMN_MOVIE_RELEASE_DATE,
                            Movie.COLUMN_MOVIE_FAVORED,
                            Movie.COLUMN_MOVIE_POSTER_PATH,
                            Movie.COLUMN_MOVIE_BACKDROP_PATH
                    },
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                final int indexMovieId = cursor.getColumnIndex(Movie.COLUMN_MOVIE_ID);
                final int indexMovieTitle = cursor.getColumnIndex(Movie.COLUMN_MOVIE_TITLE);
                final int indexMovieOverview = cursor.getColumnIndex(Movie.COLUMN_MOVIE_OVERVIEW);
                final int indexMovieVoteCount = cursor.getColumnIndex(Movie.COLUMN_MOVIE_VOTE_COUNT);
                final int indexMovieVoteAverage = cursor.getColumnIndex(Movie.COLUMN_MOVIE_VOTE_AVERAGE);
                final int indexMovieReleaseDate = cursor.getColumnIndex(Movie.COLUMN_MOVIE_RELEASE_DATE);
                final int indexMoviePosterPath = cursor.getColumnIndex(Movie.COLUMN_MOVIE_POSTER_PATH);
                final int indexMovieBackdropPath = cursor.getColumnIndex(Movie.COLUMN_MOVIE_BACKDROP_PATH);
                do {
                    MovieData movieData = new MovieData();
                    movieData.setId(cursor.getLong(indexMovieId));
                    movieData.setTitle(cursor.getString(indexMovieTitle));
                    movieData.setOverview(cursor.getString(indexMovieOverview));
                    movieData.setVoteCount(cursor.getInt(indexMovieVoteCount));
                    movieData.setVoteAverage(cursor.getDouble(indexMovieVoteAverage));
                    movieData.setReleaseDate(cursor.getString(indexMovieReleaseDate));
                    movieData.setPosterPath(cursor.getString(indexMoviePosterPath));
                    movieData.setBackdropPath(cursor.getString(indexMovieBackdropPath));
                    results.add(movieData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getFavoriteMovie: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return results;
    }

    @Override
    public void addFavoriteMovie(MovieData movieData){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(Movie.COLUMN_MOVIE_ID, movieData.getId());
            values.put(Movie.COLUMN_MOVIE_TITLE, movieData.getTitle());
            values.put(Movie.COLUMN_MOVIE_OVERVIEW, movieData.getOverview());
            values.put(Movie.COLUMN_MOVIE_VOTE_COUNT, movieData.getVoteCount());
            values.put(Movie.COLUMN_MOVIE_VOTE_AVERAGE, movieData.getVoteAverage());
            values.put(Movie.COLUMN_MOVIE_RELEASE_DATE, movieData.getReleaseDate());
            values.put(Movie.COLUMN_MOVIE_FAVORED, 1);
            values.put(Movie.COLUMN_MOVIE_POSTER_PATH, movieData.getPosterPath());
            values.put(Movie.COLUMN_MOVIE_BACKDROP_PATH, movieData.getBackdropPath());
            db.insert(Movie.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "addFavoriteMovie: ", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean isMovieFavored(String movieId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        boolean isMovieFavored = false;
        try{
            final String query = " SELECT * FROM " + Movie.TABLE_NAME
                    + " WHERE " + Movie.COLUMN_MOVIE_ID + " = ? "
                    + " AND " + Movie.COLUMN_MOVIE_FAVORED + " = 1";
            cursor = db.rawQuery(query, new String[]{movieId});
            if(cursor != null && cursor.moveToFirst()){
                isMovieFavored = true;
            }

        } catch (Exception e){
            Log.e(TAG, "isMovieFavored " , e);
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return isMovieFavored;
    }

    @Override
    public void updateFavoriteMovie(MovieData movieData){

    }

    @Override
    public void removeFavoriteMovie(String movieId){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(Movie.TABLE_NAME, Movie.COLUMN_MOVIE_ID + " = ?", new String[]{movieId});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.e(TAG, "removeFavoriteMovie : ", e);
        } finally {
            db.endTransaction();
        }

    }
}
