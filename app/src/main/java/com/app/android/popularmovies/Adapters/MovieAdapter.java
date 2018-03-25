package com.app.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.android.popularmovies.R;
import com.app.android.popularmovies.utilities.Movie;
import com.app.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {


    private List<Movie> mMovies;
    private Context mContext;
    private MovieAdapterOnClickHandler mOnMovieClickHandler;
    private int mCardWidth;


    public interface MovieAdapterOnClickHandler {
        void onMovieClick(int index);
    }


    public MovieAdapter(Context context, List<Movie> movies, MovieAdapterOnClickHandler clickHandler, int cardWidth) {
        mMovies = movies;
        mContext = context;
        mOnMovieClickHandler = clickHandler;
        mCardWidth = cardWidth;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Getting ID of movie poster item
        int movieItemID = R.layout.movie_item;

        // Creating View for inflation
        View view = LayoutInflater.from(mContext).inflate(movieItemID, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    // getting number of movies
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieAdapterViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView posterView;
        CardView cardView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);

            // getting ref for items in view holder
            posterView = itemView.findViewById(R.id.poster_image);
            cardView = itemView.findViewById(R.id.card);

            // attaching onClickListener to View holder
            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            String poster = mMovies.get(index).getMoviePoster();
            poster = NetworkUtils.buildImageURL(poster, NetworkUtils.POSTER);

            // setting item view with optimized width and height (obtained from optimizeView method in MainActivity, passed to the MovieAdapter)
            cardView.getLayoutParams().width = mCardWidth;

            // images form TheMovieDB has aspect ratio 1.5 so height = width x 1.5
            cardView.getLayoutParams().height = (int) (mCardWidth * 1.5);

            // Loading image
            Picasso.with(mContext).load(poster).into(posterView);
        }

        // Getting position of the item clicked
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnMovieClickHandler.onMovieClick(adapterPosition);
        }
    }
}
