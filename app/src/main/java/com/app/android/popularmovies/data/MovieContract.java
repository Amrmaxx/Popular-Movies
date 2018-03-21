package com.app.android.popularmovies.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.app.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    // Inner class to define Table Contents
    public static final class MovieEntry implements BaseColumns {

        // Query Uri
        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        //      movie
        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //    | _id  |  Movie ID  |  Title  |  Date  |  Rating  |  Poster  |  Plot  |
        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


        // Table Name
        public static final String TABLE_NAME = "movie";

        // Columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_VOTE_RATING = "rating";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_PLOT = "plot";

    }
}
