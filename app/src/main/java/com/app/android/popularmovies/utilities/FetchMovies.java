package com.app.android.popularmovies.utilities;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;


// Asynchronous Task to start Network Request in Background thread
public class FetchMovies extends AsyncTask<String, Void, String> {

    private Context context;
    private AsyncTaskListener<String> listener;
    private String criteria;
    private String movieID;

    private String test;

    /**
     * To run Asynchronous as separate Java class, an interface is needed to execute certain code
     * after completion of the async task. implementing a method to be called in onPostExecute
     */
    public interface AsyncTaskListener<String> {
        void onCompletion(String string);
    }

    // Constructors
    public FetchMovies(Context context, AsyncTaskListener<String> listener, String criteria) {
        this.context = context;
        this.listener = listener;
        this.criteria = criteria;
    }

    public FetchMovies(Context context, AsyncTaskListener<String> listener, String criteria, String id) {
        this.context = context;
        this.listener = listener;
        this.criteria = criteria;
        this.movieID = id;
    }

    @Override
    protected String doInBackground(String... params) {

        // Network Request

        URL url;
        switch (criteria) {
            case NetworkUtils.VIDEOS_DIRECTORY: {
                url = NetworkUtils.buildVideosURL(movieID);
                break;
            }
            case NetworkUtils.REVIEWS_DIRECTORY: {
                url = NetworkUtils.buildReviewsURL(movieID);
                break;
            }
            default:
                url = NetworkUtils.buildListURL(criteria);
        }
        test = url.toString();
        try

        {
            String jsonResponse = NetworkUtils.getResponseFromHTTP(url);
            test = jsonResponse;
            return jsonResponse;

        } catch (
                Exception e)

        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onCompletion(s);
    }
}
