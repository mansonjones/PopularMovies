package com.example.android.popularmovies2.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterFragment extends Fragment {

    private ImageAdapter mMoviePosterAdapter;

    public MoviePosterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movieinfofragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.  The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.cml
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovieInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMoviePosterAdapter = new ImageAdapter(
                getActivity(),
                new ArrayList<String>());


        // Get a reference to the GridView, and attach the adapter to it.
        GridView gridView = (GridView) rootView.findViewById(
                R.id.gridview_movieposters);
        gridView.setAdapter(mMoviePosterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Dummy Data - Please remove from final version
                String movieTitle = mMoviePosterAdapter.getItem("original_title", position);
                String moviePoster = mMoviePosterAdapter.getItem("poster_path", position);
                String movieSynopsis = mMoviePosterAdapter.getItem("overview", position);
                // String userRating = "8.1/11";
                String userRating =
                        String.valueOf(mMoviePosterAdapter.getItem("vote_average", position));
                String releaseDate = mMoviePosterAdapter.getItem("release_date", position);
               // Toast.makeText(getActivity(), movieInfo, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("MOVIE_TITLE", movieTitle)
                        .putExtra("MOVIE_POSTER", moviePoster)
                        .putExtra("MOVIE_PLOT_SYNOPSIS", movieSynopsis)
                        .putExtra("MOVIE_USER_RATING", userRating)
                        .putExtra("MOVIE_RELEASE_DATE", releaseDate);

                startActivity(intent);
            }
        });

        return rootView;

    }

    private void updateMovieInfo() {
        FetchMovieInfoTask movieInfoTask = new FetchMovieInfoTask();
        /*
        String location = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_location_default));
        */
        movieInfoTask.execute("popularity.desc");

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieInfo();
    }

    public class FetchMovieInfoTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieInfoTask.class.getSimpleName();

        private String[] getMovieInfoDataFromJson(String movieJsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String MOVIEDB_RESULTS = "results";
            final String MOVIEDB_POSTER_PATH = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MOVIEDB_RESULTS);

            String[] resultStrs = new String[movieArray.length()];
            // this is for debugging only
            // String[] resultStrs = new String[30];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieData = movieArray.getJSONObject(i);
                String movieDataString = movieData.toString();
                resultStrs[i] = movieDataString;
            }
            // This is for debugging only.
            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Movie entry: " + s);
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String sort_by = "popularity.desc";
            // can also use sort_by = "vote_average.desc";
            // Do not check in this key.  To Do: put this key in a separate file
            String api_key = "PUT_YOUR_KEY_HERE";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                // Note: Do Not checkin in any code that has this key in it.
                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI" + builtUri.toString());


                // Create the request to MovieDb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "MovieInfo JSON String: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieInfoDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the movie info
            return null;

        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mMoviePosterAdapter.clear();
                List<String> resultList = new ArrayList<String>(
                        Arrays.asList(result)
                );
                mMoviePosterAdapter.addAll(resultList);
                /*
                // This also works but is less efficient because it
                // calls notifyDataChanged for each add operation.
                for (String posterPathStr : result) {
                    mMoviePosterAdapter.add(posterPathStr);
                }
                */
            }
        }

    }
}
