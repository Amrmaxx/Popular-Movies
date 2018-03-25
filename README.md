Popular Movies App
====================

The app shows a list of movies based on (Top Rated / Popular) preference selection, default Top Rated.
On Movie Poster click app will show movie details, can be added to favorites.
User can go to favorites to see all saved movies, can also view each movie detail


API KEY
=======
API Key from TheMovieDB is required to run the App,
Place your API Key in:
	 NetworkUtils Class, Line 22 (marked inside)


Structure
=========
1-Main Activity: contains recycler view grid layout, implementing click listener and preference change listener, implement BroadCastReceiver for network to start loading if network became available calls for background thread for network. Options menu redirects to setting or favorites.

2-Movie Detail Activity: shows details of a selected movie, Trailers and reviews, option items are Share (first trailer), and add/remove movie from favorites

3-Favorite Activity: show a list of saved movies titles and poster, on click will show movie detail, on long click will select movie.

3-Movie Adapter populating Recycler View on Main Activity and implementing on Click Listener.

4-Movie Detail Adapter: populating Trailers / reviews on Detail activity implementing on Click Listener.

5-Favorites Adapter: populates saved movies implementing on Click Listener & on Long Click Listener.

6-Movie Contract/DB Helper: Database structure for movies.

7-MovieProvider: Content Provider to manage database calls.

8-Movie Class: contains Movie constructors, getter methods and setter methods, sub class for movie trailers/reviews.

9-NetworkUtils Class: builds URLs and handles network activity.

10-JsonUtils Class: Parses the movie URL.

11-FetchMovies: Class implementing asynchronous Task to start network request in background Thread.

12-Setting Activity & Fragment: handles setting menu, preferences changes and listener.

