package com.app.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.android.popularmovies.Adapters.MovieDetailAdapter;
import com.app.android.popularmovies.data.MovieContract.MovieEntry;
import com.app.android.popularmovies.utilities.FetchMovies;
import com.app.android.popularmovies.utilities.JsonUtils;
import com.app.android.popularmovies.utilities.Movie;
import com.app.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

// Activity to show a single movie Details
public class MovieDetail extends AppCompatActivity
        implements MovieDetailAdapter.MovieDetailOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    // String to be put/get with Intent
    public static final String EXTRA_POSTER = "extra_poster";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DATE = "extra_date";
    public static final String EXTRA_VOTE = "extra_vote";
    public static final String EXTRA_PLOT = "extra_plot";
    public static final String EXTRA_ID = "id";
    private RecyclerView mRecyclerView;
    private MovieDetailAdapter mAdapter;
    private int currentTab = 0;
    private List<Movie.MovieData> mMovieTrailer;
    private List<Movie.MovieData> mMovieReview;
    private int databaseID;
    private String movieID;
    private String title;
    private String releaseDate;
    private String backDrop;
    private String voteAverage;
    private String plot;

    private static final int MOVIE_DETAIL_LOADER_ID = 15;

    // This boolean is used to identify if movie is in DB or not after loading
    private static boolean movieExistsInDb = false;


    // Projection used to Insert / delete Movie from DB
    private static final String[] MOVIE_QUERY_PROJECTION = {MovieEntry.COLUMN_MOVIE_ID, MovieEntry._ID};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Getting Intent
        Intent intent = getIntent();

        // Getting Data passed with intent
        backDrop = intent.getStringExtra(EXTRA_POSTER);
        title = intent.getStringExtra(EXTRA_TITLE);
        releaseDate = intent.getStringExtra(EXTRA_DATE);
        voteAverage = intent.getStringExtra(EXTRA_VOTE);
        plot = intent.getStringExtra(EXTRA_PLOT);
        movieID = intent.getStringExtra(EXTRA_ID);

        // Getting reference of the views
        ImageView backDropIV = findViewById(R.id.back_drop_IV);
        TextView titleTV = findViewById(R.id.title_TV);
        TextView monthTV = findViewById(R.id.month_TV);
        TextView yearTV = findViewById(R.id.year_TV);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView plotTV = findViewById(R.id.plot_TV);

        // convert parsed image to valid URL
        String posterUrl = NetworkUtils.buildImageURL(backDrop, NetworkUtils.BACKDROP);

        // convert month number to name
        String shortMonthName = monthName(Integer.parseInt(releaseDate.substring(5, 7)));

        // Populating UI
        Picasso.with(this).load(posterUrl).into(backDropIV);
        titleTV.setText(title);
        yearTV.setText(releaseDate.substring(0, 4));
        monthTV.setText(shortMonthName);
        ratingBar.setRating(Float.parseFloat(voteAverage) / 2);
        plotTV.setText(plot);


        // Setting tabLayout for trailers and reviews
        final TabLayout tabLayout = findViewById(R.id.tabs);

        if (NetworkUtils.checkInternet(this)) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tabLayout.getSelectedTabPosition()) {
                        case 0: {
                            currentTab = 0;
                            trailerTab();
                            break;
                        }
                        case 1: {
                            currentTab = 1;
                            reviewTab();
                            break;
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            mRecyclerView = findViewById(R.id.movie_detail_RV);

            // Network Request to trailer tab by default at start
            trailerTab();
        } else {

            tabLayout.setVisibility(View.INVISIBLE);
        }

        //  Starting loader to find if the movie exists in the DataBase
        getSupportLoaderManager().restartLoader(MOVIE_DETAIL_LOADER_ID, null, this);
    }


    //  Network result to Load Trailers / Reviews,
    public class BackgroundTaskCompletionListener implements FetchMovies.AsyncTaskListener<String> {
        @Override
        public void onCompletion(String listString) {

            if (currentTab == 0) {
                mMovieTrailer = JsonUtils.parseVideosList(listString);
                viewTrailers();
            } else {
                mMovieReview = JsonUtils.parseReviewsList(listString);
                viewReviews();
            }
        }
    }


    // Method to start network request for trailers if data not loaded before
    private void trailerTab() {
        if (mMovieTrailer == null) {
            new FetchMovies(this, new BackgroundTaskCompletionListener(), NetworkUtils.VIDEOS_DIRECTORY, movieID).execute();
        } else {
            viewTrailers();
        }
    }

    // Method to populate UI with trailers
    private void viewTrailers() {
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieDetailAdapter(MovieDetail.this, mMovieTrailer, MovieDetail.this, 0);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Method to start network request for reviews if data not loaded before
    private void reviewTab() {
        if (mMovieReview == null) {
            new FetchMovies(this, new BackgroundTaskCompletionListener(), NetworkUtils.REVIEWS_DIRECTORY, movieID).execute();
        } else {
            viewReviews();
        }
    }

    // Method to populate UI with reviews
    private void viewReviews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MovieDetailAdapter(MovieDetail.this, mMovieReview, MovieDetail.this, 1);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    //  Method to get short month name from month number  aka 03 = MAR
    private String monthName(int monthNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthNum - 1);
        return new SimpleDateFormat("MMM").format(calendar.getTime());
    }

    //  Click listener for Trailers / Reviews
    @Override
    public void onDetailClick(int index) {

        // if trailer was clicked launch intent for video
        if (currentTab == 0) {
            Movie.MovieData movieData = mMovieTrailer.get(index);
            String key = movieData.getVideoTrailer();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkUtils.YOUTUBE_BASE_URL + key));
            startActivity(intent);
        } else {

            //  if Review was clicked, expand the lines to show all text
            View view = mRecyclerView.getLayoutManager().findViewByPosition(index);
            TextView review = view.findViewById(R.id.review_content_TV);
            if (review.getMaxLines() == 3) {
                review.setLines(0);
            } else {
                review.setLines(3);
            }
        }
    }

    // Creating setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_detail_options, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    //  Using this method to reload options menu if selected movie from main activity exists already in DB
    //  This will show filled Star in action bar instead of the empty one
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_favorites);
        if (movieExistsInDb) {
            item.setIcon(R.drawable.ic_action_star);
        } else {
            item.setIcon(R.drawable.ic_action_star_border);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // checking which option was clicked
        int id = item.getItemId();

        //  clicking star icon will add/remove movie from favorites
        if (id == R.id.action_favorites) {
            // Saving / Deleting from favorites
            if (movieExistsInDb) {
                removeFromFavorites(item);
            } else {
                addToFavorites(item);
            }
            return true;

        }
        // clicking share icon will share 1st movie trailer
        if (id == R.id.action_share) {
            // Sharing First Trailer
            Movie.MovieData movieData = mMovieTrailer.get(0);
            String key = movieData.getVideoTrailer();
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle(title)
                    .setText(Uri.parse(NetworkUtils.YOUTUBE_BASE_URL + key).toString())
                    .startChooser();
            Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // This method is called when user clicks empty star to add movie to favorites
    private void addToFavorites(MenuItem item) {

        // Setting new icon as movie going to DB
        item.setIcon(R.drawable.ic_action_star);
        movieExistsInDb = true;

        //  Creating insert uri
        Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(movieID).build();

        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //    | _id  |  Movie ID  |  Title  |  Date  |  Rating  |  Poster  |  Plot  |
        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movieID);
        contentValues.put(MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        contentValues.put(MovieEntry.COLUMN_VOTE_RATING, voteAverage);
        contentValues.put(MovieEntry.COLUMN_POSTER, backDrop);
        contentValues.put(MovieEntry.COLUMN_PLOT, plot);
        Uri returnUri = getContentResolver().insert(uri, contentValues);

        // getting the databaseID _ID for the movie inserted, to be used in case user deleted the same movie again from detail activity
        databaseID = Integer.parseInt(returnUri.getLastPathSegment());
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavorites(MenuItem item) {

        // Setting new icon as movie going out DB
        item.setIcon(R.drawable.ic_action_star_border);
        movieExistsInDb = false;

        // Creating delete Uri with movie ID
        String idString = Integer.toString(databaseID);
        Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(idString).build();
        getContentResolver().delete(uri, null, null);
        Toast.makeText(this, "Removed From Favorites", Toast.LENGTH_SHORT).show();
    }

    // Loader to find if movie is in DB at start of activity
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_DETAIL_LOADER_ID:
                String selection = MovieEntry.COLUMN_MOVIE_ID + " = " + movieID;
                return new CursorLoader(this, MovieEntry.CONTENT_URI, MOVIE_QUERY_PROJECTION, selection, null, null);

            default:
                throw new RuntimeException("Loader Error " + id);
        }
    }

    // if movie is in DB already, reload options menu and get the movie _ID in DB
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            movieExistsInDb = true;
            invalidateOptionsMenu();
            int idIndex = data.getColumnIndex(MovieEntry._ID);
            data.moveToPosition(0);
            databaseID = data.getInt(idIndex);
        } else {
            movieExistsInDb = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


