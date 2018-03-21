Popular Movies App
====================

The app shows a list of movies based on (Top Rated / Popular) preference selection, default Top Rated.


API KEY
=======
API Key from TheMovieDB is required to run the App,
Place your API Key in:
	 NetworkUtils Class, Line 20 (marked inside)


Structure
=========
1-Main Activity: contains recycler view grid layout, implementing click listener and preference change listener, calls for background thread for network.

2-Movie Detail Activity: shows details of a selected movie.

3-Movie Adapter populating Recycler View on Main Activity and implementing on Click Listener.

4-Movie Class: contains Movie constructors, getter methods and setter methods.

5-NetworkUtils Class: builds URLs and handles network activity

6-JsonUtils Class: Parses the movie URL.

7-Setting Activity & Fragment: handles setting menu, preferences changes and listener.

