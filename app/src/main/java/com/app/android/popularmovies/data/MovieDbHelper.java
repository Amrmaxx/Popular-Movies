package com.app.android.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.android.popularmovies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    // Setting DataBase Name & Version
    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating SQL Table

        //      movie
        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //    | _id  |  Movie ID  |  Title  |  Date  |  Rating  |  Poster  |  Plot  |
        //     - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        final String SQL_CREATE_MOVIE_TABLE=            "CREATE TABLE " +
                        MovieEntry.TABLE_NAME           + " (" +
                        MovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID      + " INTEGER NOT NULL UNIQUE, " +
                        MovieEntry.COLUMN_TITLE         + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE  + " TEXT, " +
                        MovieEntry.COLUMN_VOTE_RATING   + " TEXT, " +
                        MovieEntry.COLUMN_POSTER        + " TEXT, " +
                        MovieEntry.COLUMN_PLOT          + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);

//        if (oldVersion<2) {
//            db.execSQL("ALTER TABLE "+ MovieEntry.TABLE_NAME + " ADD COLUMN "+ MovieEntry.COLUMN_PLOT+ " TEXT;");
//        }
//        if (oldVersion<3) {
//            db.execSQL("ALTER TABLE "+ MovieEntry.TABLE_NAME + " ADD COLUMN "+ MovieEntry.COLUMN_POSTER+ " TEXT;");
//        }
    }
}
