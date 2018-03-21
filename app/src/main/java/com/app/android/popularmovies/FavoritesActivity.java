package com.app.android.popularmovies;

import android.content.ContentValues;
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

public class FavoritesActivity
        extends AppCompatActivity
        implements FavoritesCursorAdapter.FavoritesClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private FavoritesCursorAdapter mAdapter;
    private static final int MOVIE_LOADER_ID = 7;
    public int testInteger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mRecyclerView = findViewById(R.id.favorites_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        mAdapter = new FavoritesCursorAdapter(this, FavoritesActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                //Deleting movie from fav and updating the RecyclerView
                int position = (int) viewHolder.itemView.getTag();
                String idString = Integer.toString(position);
                Uri uri = MovieEntry.CONTENT_URI.buildUpon().appendPath(idString).build();
                getContentResolver().delete(uri, null, null);
                mAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);


    }

    @Override
    public void onFavoriteClick(int index) {
        Toast.makeText(this, "Favorite " + index + " was clicked", Toast.LENGTH_SHORT).show();
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
        // checking which option was clicked
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Just for Testing
    public void fabClicked(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, testInteger);
        contentValues.put(MovieEntry.COLUMN_TITLE, "Zeroberto: " + testInteger);
        testInteger += 1;

        Uri uri = getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIE_LOADER_ID:
                return new CursorLoader(this, MovieEntry.CONTENT_URI, null, null, null, MovieEntry._ID);
            default:
                throw new RuntimeException("Loader Error " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
