package com.app.android.popularmovies.utilities;

//  Movie Class to handle movie data for details

public class Movie {

    private String movieTitle;
    private String releaseDate;
    private String moviePoster;
    private String voteAverage;
    private String plot;
    private String backdrop_path;
    private String movieID;


    // Constructors
    public Movie() {
    }

    public Movie(String movieTitle, String releaseDate, String moviePoster, String voteAverage, String plot, String backdrop_path, String movieID) {
        this.movieTitle = movieTitle;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plot = plot;
        this.backdrop_path = backdrop_path;
        this.movieID = movieID;
    }

    // Getter methods
    public String getMovieTitle() {
        return movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    // Images parsed has extra character "\" at start, using substring to remove it
    public String getMoviePoster() {
        return moviePoster.substring(1);
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlot() {
        return plot;
    }

    public String getBackdrop_path() {
        return backdrop_path.substring(1);
    }

    public String getMovieID() {
        return movieID;
    }


    // Setter Methods
    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }



    // this class is used to save Movie Trailers(trailer)/Reviews(author, content)
    public static class MovieData {
        private String trailer;
        private String author;
        private String content;

        public MovieData(String trailer) {
            this.trailer = trailer;
        }

        public MovieData(String author, String content) {
            this.author = author;
            this.content = content;
        }

        public String getVideoTrailer() {
            return this.trailer;
        }

        public String getReviewAuthor() {
            return this.author;
        }

        public String getReviewContent() {
            return this.content;
        }
    }

}
