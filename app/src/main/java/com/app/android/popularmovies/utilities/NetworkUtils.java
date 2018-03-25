package com.app.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// Class to Handle Network requests
public final class NetworkUtils {

    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/";
    private static final String MOVIE_DIRECTORY_URL = "movie";

    // insert your API key Here :)
    private static final String API_KEY = "";  //  <--- API Key here between ""

    private static final String API_KEY_PARAM = "api_key";

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String POSTER_IMAGE_SIZE = "w185";
    private static final String BACKDROP_IMAGE_SIZE = "w780";

    public static final String VIDEOS_DIRECTORY = "videos";
    public static final String REVIEWS_DIRECTORY = "reviews";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";


    /**
     * Two integers to differentiate between two image sizes:
     * For Posters (on Recycler view) using w185
     * For Backdrop (on Movie Detail Activity) using w500
     */
    public final static int POSTER = 0;
    public final static int BACKDROP = 1;

    // Method to Build network request URL for movie list
    public static URL buildListURL(String sortBy) {

        Uri movieListUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(MOVIE_DIRECTORY_URL)
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(movieListUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Method to do the network request
    public static String getResponseFromHTTP(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Method to Build a valid Image URL from parsed String
    public static String buildImageURL(String imageContent, int type) {

        // Deciding which image size to load
        String imageWidth = "";
        switch (type) {
            case POSTER:
                imageWidth = POSTER_IMAGE_SIZE;
                break;
            case BACKDROP: {
                imageWidth = BACKDROP_IMAGE_SIZE;
            }
            break;
        }

        // Building Image URL
        Uri movieListUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(imageWidth)
                .appendPath(imageContent).build();
        URL url = null;
        try {
            url = new URL(movieListUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url.toString();
    }


    // Building Trailer URL
    public static URL buildVideosURL(String movieID) {
        Uri videoUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(MOVIE_DIRECTORY_URL)
                .appendPath(movieID)
                .appendPath(VIDEOS_DIRECTORY)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(videoUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Building Review URL
    public static URL buildReviewsURL(String movieID) {
        Uri reviewUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(MOVIE_DIRECTORY_URL)
                .appendPath(movieID)
                .appendPath(REVIEWS_DIRECTORY)
                .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(reviewUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Public method to check internet availability returns true if internet is available
    public static boolean checkInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;

    }
}




