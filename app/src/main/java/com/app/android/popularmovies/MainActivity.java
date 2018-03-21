package com.app.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.android.popularmovies.Adapters.MovieAdapter;
import com.app.android.popularmovies.utilities.FetchMovies;
import com.app.android.popularmovies.utilities.JsonUtils;
import com.app.android.popularmovies.utilities.Movie;

import java.util.List;

public class MainActivity extends
        AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private List<Movie> mMovies;
    private int cardWidth;
    private ProgressBar mProgressBar;
    private TextView errorTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Testing Fav. activity
//        Intent intent = new Intent(this, FavoritesActivity.class);
//        startActivity(intent);




        // Getting Ref to UI components
        mRecyclerView = findViewById(R.id.movie_grid_view);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        errorTV = findViewById(R.id.errorTV);

        // This method is to calculate width of child views and number of grid columns with respect to screen width
        int[] data = optimizeView();
        cardWidth = data[0];
        int columns = data[1];

        // Setting layout Manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Registering shared preference change listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        String sortBy = sharedPreferences.getString(getResources().getString(R.string.pref_sort_key), getResources().getString(R.string.pref_sort_value_top_rated));

        // Shows loading indicator
        showLoading();

        // check for internet status
        checkInternet();
        // Starting Async Task
        new FetchMovies(this, new BackgroundTaskCompletionListener(), sortBy).execute();

    }

    public class BackgroundTaskCompletionListener implements FetchMovies.AsyncTaskListener<String> {
        @Override
        public void onCompletion(String moviesListString) {
            mMovies = JsonUtils.parseMoviesList(moviesListString);

            // Loading Adapter after data arrives
            mAdapter = new MovieAdapter(MainActivity.this, mMovies, MainActivity.this, cardWidth);
            mRecyclerView.setAdapter(mAdapter);
            // removing indicator and showing Recycler view
            finishedLoading();
        }
    }


    // In case Preferences changed reload movie list
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        showLoading();
        String sortBy = sharedPreferences.getString(key, "");
        new FetchMovies(this, new BackgroundTaskCompletionListener(), sortBy).execute();
    }


    /**
     * On Click Handler
     * Getting Clicked Movie Index
     * Passing Movie Details as Strings
     * Starting MovieDetail Activity Via Intent
     */
    @Override
    public void onMovieClick(int index) {
        Movie movie = mMovies.get(index);
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_POSTER, movie.getBackdrop_path());
        intent.putExtra(MovieDetail.EXTRA_TITLE, movie.getMovieTitle());
        intent.putExtra(MovieDetail.EXTRA_DATE, movie.getReleaseDate());
        intent.putExtra(MovieDetail.EXTRA_VOTE, movie.getVoteAverage());
        intent.putExtra(MovieDetail.EXTRA_PLOT, movie.getPlot());
        intent.putExtra(MovieDetail.EXTRA_ID, movie.getMovieID());
        startActivity(intent);
    }


    // Creating setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_list_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // checking which option was clicked
        int id = item.getItemId();

        if (id == R.id.action_favorites) {
            // Starting favorites activity
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;

        }
        if (id == R.id.action_settings) {
            // Starting settings Activity
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // This method is to calculate width of child views and number of grid columns with respect to screen width
    private int[] optimizeView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Getting screen Density
        float density = displayMetrics.density;
        // Getting Screen With
        int width = Math.round(displayMetrics.widthPixels / density);

        /*
         * If number n of views will fit screen by width then you have n+1 spaces in between
         * then screen width  =  n x number of views + (n+1) x number of spaces
         * spacing between views will be 4 dp,
         * number of views (by width)  minimum 2 and  maximum 9
         * card width will be between 120dp and 190dp
         * if more than result will fit then bigger card size will be selected.
         * the method will return optimum card width & optimum number of grid columns
         */
        int columns = 2;
        double comparedCardWidth = 0.99;
        for (int i = 2; i < 9; i++) {
            cardWidth = Math.round((width - (i + 1) * 4) / i);
            if (cardWidth > 120 && cardWidth < 190 && cardWidth % 1 < comparedCardWidth % 1) {
                comparedCardWidth = cardWidth;
                columns = i;
            }
        }
        cardWidth = (int) (comparedCardWidth * density);
        return new int[]{cardWidth, columns};
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void finishedLoading() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // If no connection showing error string
        if (!isConnected) {
            errorTV.setText(getResources().getString(R.string.no_connection));
            errorTV.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister Shared preference change listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


}

