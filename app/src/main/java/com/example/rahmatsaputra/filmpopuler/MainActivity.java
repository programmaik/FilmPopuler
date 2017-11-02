package com.example.rahmatsaputra.filmpopuler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rahmatsaputra.filmpopuler.data.api.TmdbService;
import com.example.rahmatsaputra.filmpopuler.data.db.MovieRepository;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieData;
import com.example.rahmatsaputra.filmpopuler.data.model.MovieDataResponse;
import com.example.rahmatsaputra.filmpopuler.features.main.adapter.MainAdapter;
import com.example.rahmatsaputra.filmpopuler.features.main.contract.AddOrDeleteFavoriteMovieContract;
import com.example.rahmatsaputra.filmpopuler.features.main.contract.MainListItemClickListener;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MainItem;
import com.example.rahmatsaputra.filmpopuler.features.main.model.MovieItem;
import com.example.rahmatsaputra.filmpopuler.utils.converter.MovieDataToMainItemConverter;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    public static final String KEY_CURRENT_STATE = "CURRENT_STATE";

    public static final int STATE_POPULAR_MOVIE = 0;
    public static final int STATE_FAVORITE_MOVIE = 1;
    public static final int STATE_TOPRATE_MOVIE = 2;

    TextView mainMessageField;
    ProgressBar mainProgressBar;
    RecyclerView mainListView;

    MainAdapter mainAdapter;
    BottomNavigationView bottomNavigationView;

    FavoriteBroadcastReceiver favoriteBroadcastReceiver;
    MainListItemClickListener mainListItemClickListener;
    GridLayoutManager layoutManager;
    Toast toast;
    MovieRepository movieRepository;
    List<MovieData> movieDataList;
    int currentState = STATE_POPULAR_MOVIE;

    public static final String KEY_MOVIENAME ="movieName";
    public static final String KEY_RELEASEDATE = "releaseDate";
    public static final String KEY_POSTERPATH = "posterPath";
    public static final String KEY_DESCRIPTION = "overview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        if (savedInstanceState != null) {
            currentState = savedInstanceState.getInt(KEY_CURRENT_STATE, STATE_POPULAR_MOVIE);
        }

        bindViews();
        setUpMainListView();
        if (currentState == STATE_POPULAR_MOVIE) {
            fetchPopularMovie();
        } else if (currentState == STATE_FAVORITE_MOVIE){
            fetchFavoriteMovie();
        } else {
            fetchTopRatedMovieLoader();
        }

        favoriteBroadcastReceiver = new FavoriteBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE);
        intentFilter.addAction(AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE);
        LocalBroadcastManager.getInstance(this).registerReceiver(favoriteBroadcastReceiver, intentFilter);

    }

    LoaderManager.LoaderCallbacks<List<MovieData>> favoriteList = new LoaderManager.LoaderCallbacks<List<MovieData>>() {
        @Override
        public Loader<List<MovieData>> onCreateLoader(int i, Bundle bundle) {
            return new  ListFavoriteMovieLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> movieDataList) {
            hideProgressBar();
            if (movieDataList != null && !movieDataList.isEmpty()) {
                showMovieList(
                        MovieDataToMainItemConverter.getMainItemList("Favorite Movies", movieDataList)
                );
            } else {
                showEmptyMovieList();
            }
        }

        @Override
        public void onLoaderReset(Loader<List<MovieData>> loader) {

        }
    };

    LoaderManager.LoaderCallbacks<List<MovieData>> topRatedMovieList = new LoaderManager.LoaderCallbacks<List<MovieData>>() {
        @Override
        public Loader<List<MovieData>> onCreateLoader(int i, Bundle bundle) {
            return new TopRatedMovieListLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> movieDataList) {
            hideProgressBar();
            Call<MovieDataResponse> call = TmdbService.open().getTopRatedMovies(1);
            call.enqueue(new Callback<MovieDataResponse>() {
                @Override
                public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                    MovieDataResponse data = response.body();
                    hideProgressBar();
                    if (data != null) {
                        List<MovieData> movies = data.getMovieDataList();
                        if (movies != null && !movies.isEmpty()) {
                            showMovieList(
                                    MovieDataToMainItemConverter.getMainItemList("Top Rated Movies", movies)

                            );
                        } else {
                            showEmptyMovieList();
                        }
                    } else {
                        showMessage("Something wrong happened");
                    }
                }

                @Override
                public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                    hideProgressBar();
                    showMessage(t.getMessage());
                }
            });
        }

        @Override
        public void onLoaderReset(Loader<List<MovieData>> loader) {

        }
    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_STATE, currentState);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(favoriteBroadcastReceiver);
    }

    private void bindViews(){
        mainMessageField = findViewById(R.id.main_message);
        mainListView = findViewById(R.id.main_movie_listview);
        mainProgressBar = findViewById(R.id.main_progressbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setUpMainListView(){
        mainListItemClickListener = new MainListItemClickListener(){

            @Override
            public void onMovieItemClick(MovieItem movieItem) {

                MovieDetailActivity.showMovieDetailPage(
                        MainActivity.this,
                        movieItem.getMovieId(),
                        movieItem.getMovieTitle(),
                        movieItem.getPosterPath(),
                        movieItem.getMovieData()
                );
            }
        };
        mainAdapter = new MainAdapter(mainListItemClickListener);
        mainListView.setAdapter(mainAdapter);
        mainListView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                return position != RecyclerView.NO_POSITION ? mainAdapter.getItemSize(position) : 1;
            }
        });
        mainListView.setLayoutManager(layoutManager);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                final int menuId = menuItem.getItemId();
                if (menuId == R.id.action_popular) {
                    currentState = STATE_POPULAR_MOVIE;
                    fetchPopularMovie();
                } else if (menuId == R.id.action_favorites){
                    currentState = STATE_FAVORITE_MOVIE;
                    fetchFavoriteMovie();
                } else {
                    currentState = STATE_TOPRATE_MOVIE;
                    fetchTopRatedMovieLoader();
                }
                return true;
            }
        });
        /*bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView
                .OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                // do nothing
            }
        });*/
    }
    private void showMovieList(List<MainItem> mainItemList){
        mainAdapter.setMainItemList(mainItemList);
        mainListView.setVisibility(View.VISIBLE);
    }
    private void hideMovieList() {
        mainListView.setVisibility(View.GONE);
    }
    private void showEmptyMovieList() {
        mainAdapter.setMainItemList(Collections.<MainItem>emptyList());
        showMessage("Empty movie list");
    }

    private void showProgressBar() {
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mainProgressBar.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        mainMessageField.setText(message);
        mainMessageField.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        mainMessageField.setText(null);
        mainMessageField.setVisibility(View.GONE);
    }

    private void showToastMessage(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void fetchPopularMovie() {
        showProgressBar();
        hideMovieList();
        hideMessage();
        Call<MovieDataResponse> call = TmdbService.open().getMostPopularMovies(1);
        call.enqueue(new Callback<MovieDataResponse>() {
            @Override
            public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                MovieDataResponse data = response.body();
                hideProgressBar();
                if (data != null) {
                    List<MovieData> movies = data.getMovieDataList();
                    if (movies != null && !movies.isEmpty()) {
                        showMovieList(
                                MovieDataToMainItemConverter.getMainItemList("Popular Movies", movies)

                        );
                    } else {
                        showEmptyMovieList();
                    }
                } else {
                    showMessage("Something wrong happened");
                }
            }

            @Override
            public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                hideProgressBar();
                showMessage(t.getMessage());
            }
        });
    }

    //Tanpa AsyncTaskLoader
    private void fetchTopRatedMovie() {
        showProgressBar();
        hideMovieList();
        hideMessage();
        Call<MovieDataResponse> call = TmdbService.open().getTopRatedMovies(1);
        call.enqueue(new Callback<MovieDataResponse>() {
            @Override
            public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                MovieDataResponse data = response.body();
                hideProgressBar();
                if (data != null) {
                    List<MovieData> movies = data.getMovieDataList();
                    if (movies != null && !movies.isEmpty()) {
                        showMovieList(
                                MovieDataToMainItemConverter.getMainItemList("Top Rated Movies", movies)

                        );
                    } else {
                        showEmptyMovieList();
                    }
                } else {
                    showMessage("Something wrong happened");
                }
            }

            @Override
            public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                hideProgressBar();
                showMessage(t.getMessage());
            }
        });
    }
    private void fetchFavoriteMovie() {
        showProgressBar();
        hideMessage();
        hideMovieList();
        getSupportLoaderManager().restartLoader(
                ListFavoriteMovieLoader.FAVORITE_MOVIE_LIST_LOADER_ID,
                null,
                favoriteList
        );
    }

    // Dengan AsyncTaskLoader
    private void fetchTopRatedMovieLoader(){
        showProgressBar();
        hideMessage();
        hideMovieList();
        getSupportLoaderManager().restartLoader(
                TopRatedMovieListLoader.TOPRATED_MOVIE_LIST_LOADER_ID,
                null,
                topRatedMovieList
        );

    }
    class FavoriteBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                final String action = intent.getAction();
                if (AddOrDeleteFavoriteMovieContract.ACTION_ADD_FAVORITE.equals(action) || AddOrDeleteFavoriteMovieContract.ACTION_DELETE_FAVORITE.equals(action)){
                    if (currentState == STATE_FAVORITE_MOVIE){
                        getSupportLoaderManager().restartLoader(
                                ListFavoriteMovieLoader.FAVORITE_MOVIE_LIST_LOADER_ID,null,favoriteList
                        );
                    }
                }
            }
        }
    }
}
