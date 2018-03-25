package com.app.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.android.popularmovies.Adapters.FavoritesCursorAdapter;
import com.app.android.popularmovies.data.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity
        extends AppCompatActivity
        implements FavoritesCursorAdapter.FavoritesClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private FavoritesCursorAdapter mAdapter;
    private Cursor mCursor;
    private static final int MOVIE_LOADER_ID = 7;

    // List to save selected movies to delete more than one at once
    private List<Integer> mSelection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Getting ref to Recycler view
        mRecyclerView = findViewById(R.id.favorites_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Creating / setting cursor adapter
        mAdapter = new FavoritesCursorAdapter(this, FavoritesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        // setting divider decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Setting item touch helper to be used to delete movie on swipe
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                //  Deleting movie from fav and updating the RecyclerView
                int position = (int) viewHolder.itemView.getTag();
                String idString = Integer.toString(position);
                Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(idString).build();
                getContentResolver().delete(uri, null, null);
                mAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // Loading movie from DB
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    // Closing cursor on leaving
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursor.close();
    }

    // Starting Detail activity via intent if a movie was clicked
    @Override
    public void onFavoriteClick(int index) {

        mCursor.moveToPosition(index);

        int movieIdIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
        int titleIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
        int dateIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
        int ratingIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_VOTE_RATING);
        int plotIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_PLOT);
        int posterIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER);

        String movieID = String.valueOf(mCursor.getInt(movieIdIndex));
        String title = mCursor.getString(titleIndex);
        String date = mCursor.getString(dateIndex);
        String rating = mCursor.getString(ratingIndex);
        String plot = mCursor.getString(plotIndex);
        String poster = mCursor.getString(posterIndex);

        mCursor.close();

        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra(MovieDetail.EXTRA_POSTER, poster);
        intent.putExtra(MovieDetail.EXTRA_TITLE, title);
        intent.putExtra(MovieDetail.EXTRA_DATE, date);
        intent.putExtra(MovieDetail.EXTRA_VOTE, rating);
        intent.putExtra(MovieDetail.EXTRA_PLOT, plot);
        intent.putExtra(MovieDetail.EXTRA_ID, movieID);
        startActivity(intent);
    }

    //  implementing onLongClickListener to select multiple movies for deletion
    @Override
    public void onHold(int index) {
        View itemView = mRecyclerView.getChildAt(index);

        //  Creating a list to save selected items to be deleted.
        //  if movie is selected before remove selection
        if (mSelection.contains(index)) {
            mSelection.remove(mSelection.indexOf(index));
            itemView.setBackgroundColor(getResources().getColor(R.color.light));
        } else {
            //  if movie is not already selected then select
            mSelection.add(index);
            itemView.setBackgroundColor(getResources().getColor(R.color.selection));
        }
    }

    // Creating setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.favorites_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Deleting selected movies
        if (id == R.id.action_delete) {
            if (mSelection.size() == 0) {
                Toast.makeText(this, "Select Movies First to Delete", Toast.LENGTH_SHORT).show();
            } else {

                // Loop throws selected movies to delete
                for (int i = 0; i < mSelection.size(); i++) {
                    mCursor.moveToPosition(mSelection.get(i));
                    int idIndex = mCursor.getColumnIndex(MovieEntry._ID);
                    String idString = String.valueOf(mCursor.getInt(idIndex));
                    Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(idString).build();
                    getContentResolver().delete(uri, null, null);
                }
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Loader to load movies from DB
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_LOADER_ID:
                return new CursorLoader(this, MovieEntry.CONTENT_URI, null, null, null, MovieEntry._ID);
            default:
                throw new RuntimeException("Loader Error " + id);
        }
    }

    //  Updating adapter upon load
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
