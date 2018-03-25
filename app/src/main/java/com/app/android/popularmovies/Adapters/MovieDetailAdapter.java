package com.app.android.popularmovies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.android.popularmovies.R;
import com.app.android.popularmovies.utilities.Movie;

import java.util.List;

// This adapter is used to populate Trailers and reviews
public class MovieDetailAdapter extends RecyclerView.Adapter<MovieDetailAdapter.MovieDetailAdapterViewHolder> {

    private List<Movie.MovieData> mList;
    private Context mContext;
    private MovieDetailOnClickHandler mOnMovieDetailClickHandler;
    private int mTabNum;


    public interface MovieDetailOnClickHandler {
        void onDetailClick(int index);
    }

    public MovieDetailAdapter(Context context, List<Movie.MovieData> list, MovieDetailOnClickHandler clickHandler, int tabNum) {
        mContext = context;
        mList = list;
        mOnMovieDetailClickHandler = clickHandler;
        mTabNum = tabNum;
    }

    @Override
    public MovieDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Getting ID the current tab to select layout
        int movieItemID;
        if (mTabNum == 0) {
            movieItemID = R.layout.trailer_item;
        } else {
            movieItemID = R.layout.review_item;
        }


        // Creating View for inflation
        View view = LayoutInflater.from(mContext).inflate(movieItemID, parent, false);
        return new MovieDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDetailAdapterViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MovieDetailAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //  Trailer has 1 text view: Title
        TextView trailer;
        //  Review got 2 text views: Author and content
        TextView author;
        TextView content;

        public MovieDetailAdapterViewHolder(View itemView) {
            super(itemView);
            trailer = itemView.findViewById(R.id.trailer_TV);
            author = itemView.findViewById(R.id.review_author_TV);
            content = itemView.findViewById(R.id.review_content_TV);
            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            if (mTabNum == 0) {
                trailer.setText("Trailer " + (index + 1));
            } else {
                Movie.MovieData movieData = mList.get(index);
                author.setText(movieData.getReviewAuthor());
                content.setText(movieData.getReviewContent());
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnMovieDetailClickHandler.onDetailClick(adapterPosition);

        }
    }
}
