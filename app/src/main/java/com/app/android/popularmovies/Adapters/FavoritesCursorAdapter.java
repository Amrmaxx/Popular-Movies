package com.app.android.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.android.popularmovies.R;
import com.app.android.popularmovies.data.MovieContract.MovieEntry;


public class FavoritesCursorAdapter extends RecyclerView.Adapter<FavoritesCursorAdapter.FavoritesViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private FavoritesClickHandler mOnFavoriteClickHandler;

    public interface FavoritesClickHandler {
        void onFavoriteClick(int index);
    }

    public FavoritesCursorAdapter(Context context, FavoritesClickHandler clickHandler) {
        mContext = context;
        mOnFavoriteClickHandler = clickHandler;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int favoriteItemID = R.layout.favorite_item;
        View view = LayoutInflater.from(mContext).inflate(favoriteItemID, parent, false);
        FavoritesViewHolder viewHolder = new FavoritesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(MovieEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String title = mCursor.getString(titleIndex);

        holder.itemView.setTag(id);
        holder.favoriteMovieTitle.setText(title);
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
            implements View.OnClickListener {

        TextView favoriteMovieTitle;

        public FavoritesViewHolder(View itemView) {
            super(itemView);
            favoriteMovieTitle = itemView.findViewById(R.id.favorite_item_TV);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            mCursor.moveToPosition(adapterPosition);
            mOnFavoriteClickHandler.onFavoriteClick(adapterPosition);

        }
    }
}
