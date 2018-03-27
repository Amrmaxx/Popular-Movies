package com.app.android.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.popularmovies.R;
import com.app.android.popularmovies.data.MovieContract.MovieEntry;
import com.app.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//  This class will be used to populate UI with favorite movies
public class FavoritesCursorAdapter extends RecyclerView.Adapter<FavoritesCursorAdapter.FavoritesViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private FavoritesClickHandler mOnFavoriteClickHandler;
    private List<Integer> mSelection = new ArrayList<>();

    public interface FavoritesClickHandler {
        void onFavoriteClick(int index);

        void onHold(int index);
    }

    public FavoritesCursorAdapter(Context context, FavoritesClickHandler clickHandler, List<Integer> selection) {
        mContext = context;
        mOnFavoriteClickHandler = clickHandler;
        mSelection = selection;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int favoriteItemID = R.layout.favorite_item;
        View view = LayoutInflater.from(mContext).inflate(favoriteItemID, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        // Getting movie ID, Title, Poster from cursor
        int idIndex = mCursor.getColumnIndex(MovieEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
        int posterIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String title = mCursor.getString(titleIndex);
        String poster = mCursor.getString(posterIndex);

        String posterUrl = NetworkUtils.buildImageURL(poster, NetworkUtils.BACKDROP);

        // Populating UI

        //  Show image in the favorite item layout with low alpha
        Picasso.with(mContext).load(posterUrl).error(R.drawable.error).into(holder.favPoster);
        //  setting Tag
        holder.itemView.setTag(id);
        //  Setting title
        holder.favoriteMovieTitle.setText(title);
        if (mSelection.contains(position)) {
            holder.favPoster.setAlpha((float) 0.5);

        } else {
            holder.favPoster.setAlpha((float) 0.99);
        }
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();

    }

    //  Function to swap old cursor with new one in case re-query
    public Cursor swapCursor(Cursor c) {

        // If new = old then nothing changed
        if (mCursor == c) {
            return null;
        }
        Cursor tempCursor = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return tempCursor;
    }

    public class FavoritesViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        TextView favoriteMovieTitle;
        ImageView favPoster;

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            favoriteMovieTitle = itemView.findViewById(R.id.favorite_item_TV);
            favPoster = itemView.findViewById(R.id.fav_poster);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnFavoriteClickHandler.onFavoriteClick(adapterPosition);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnFavoriteClickHandler.onHold(adapterPosition);
            return true;
        }
    }
}
