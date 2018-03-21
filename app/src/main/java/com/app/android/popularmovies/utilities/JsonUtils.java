package com.app.android.popularmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // Method to Parse Movie list (Titles and images)
    public static List<Movie> parseMoviesList(String jsonMovieList) {

        // Creating array list to save movies
        List<Movie> Movies = new ArrayList<>();

        // JSON Parsing
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonMovieList);
            JSONArray movieList = baseJsonResponse.optJSONArray("results");

            for (int i = 0; i < movieList.length(); i++) {
                JSONObject movieItem = movieList.optJSONObject(i);
                String title = movieItem.optString("title");
                String moviePoster = movieItem.optString("poster_path");
                String release_date = movieItem.optString("release_date");
                String vote_average = movieItem.optString("vote_average");
                String plot = movieItem.optString("overview");
                String backdrop_path = movieItem.optString("backdrop_path");
                String movieID = movieItem.optString("id");

                Movie movie = new Movie(title, release_date, moviePoster, vote_average, plot, backdrop_path, movieID);
                Movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e("JsonUtils", "Problem parsing", e);
        }
        return Movies;
    }


    public static List<Movie.MovieData> parseVideosList(String jsonVideosList) {

        // Creating array list to save videos
        List<Movie.MovieData> videos = new ArrayList<>();

        // JSON Parsing
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonVideosList);
            JSONArray reviewList = baseJsonResponse.optJSONArray("results");

            for (int i = 0; i < reviewList.length(); i++) {
                JSONObject videoItem = reviewList.optJSONObject(i);
                String trailer = videoItem.optString("key");
                Movie.MovieData video = new Movie.MovieData(trailer);
                videos.add(video);
            }
        } catch (JSONException e) {
            Log.e("JsonUtils", "Problem parsing", e);
        }
        return videos;

    }


    public static List<Movie.MovieData> parseReviewsList(String jsonReviewsList) {

        // Creating array list to save videos
        List<Movie.MovieData> Reviews = new ArrayList<>();

        // JSON Parsing
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonReviewsList);
            JSONArray reviewsList = baseJsonResponse.optJSONArray("results");

            for (int i = 0; i < reviewsList.length(); i++) {
                JSONObject videoItem = reviewsList.optJSONObject(i);
                String author = videoItem.optString("author");
                String content = videoItem.optString("content");
                Movie.MovieData review = new Movie.MovieData(author, content);
                Reviews.add(review);

            }
        } catch (JSONException e) {
            Log.e("JsonUtils", "Problem parsing", e);
        }
        return Reviews;

    }
}




