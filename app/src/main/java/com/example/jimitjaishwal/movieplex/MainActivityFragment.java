package com.example.jimitjaishwal.movieplex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MovieAdapter mDbAdapter;
    ArrayList<MovieData> movieList;
    @Bind(R.id.movie_item_grid)
    GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("MovieData")) {
            movieList = new ArrayList<MovieData>();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("MovieData");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("MovieData", movieList);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDbAdapter.clear();
        upDateList();
    }

    public void upDateList() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        mDbAdapter = new MovieAdapter(getActivity(), movieList);

        gridView.setAdapter(mDbAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieData mData = mDbAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("MovieData", mData);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<MovieData>> {

        private String LOG_TAG = FetchMovieTask.class.getSimpleName();
        ArrayList<MovieData> results = new ArrayList<>();

        public List<MovieData> getMovieDataFromJson(String forecastJsonStr) throws JSONException {

            final String mDB_POSTER_URL = "http://image.tmdb.org/t/p/w185";
            final String mDB_Array = "results";
            final String mDB_posterPath = "poster_path";
            final String mDB_overview = "overview";
            final String mDB_releaseDate = "release_date";
            final String mDB_originalTitle = "original_title";
            final String mDB_backdrop_path = "backdrop_path";
            final String mDB_vote_average = "vote_average";
            JSONObject mDataObj = new JSONObject(forecastJsonStr);
            JSONArray mDataArray = mDataObj.getJSONArray(mDB_Array);

            for (int i = 0; i < mDataArray.length(); i++) {
                JSONObject mList = mDataArray.getJSONObject(i);
                String posterPath = mList.getString(mDB_posterPath);
                String overView = mList.getString(mDB_overview);
                String releaseDate = mList.getString(mDB_releaseDate);
                String originalTitle = mList.getString(mDB_originalTitle);
                String backdrop_path = mList.getString(mDB_backdrop_path);
                String vote_average = mList.getString(mDB_vote_average);

                MovieData movie = new MovieData(
                        mDB_POSTER_URL,
                        posterPath, originalTitle,
                        overView, releaseDate,
                        vote_average, backdrop_path);
                results.add(movie);

                String poster = mDB_POSTER_URL + posterPath;
                Log.v(LOG_TAG, "Poster URL : " + poster);
            }
            return results;
        }

        @Override
        protected List<MovieData> doInBackground(String... params) {

            if (params == null) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String api_key = "149c3ccaa00eeb61331e4da7734fd1af";

            try {
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sort_by = prefs.getString(
                        getString(R.string.pref_movie_list_sort_by_key),
                        getString(R.string.pref_popularity));

                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, sort_by)
                        .appendQueryParameter(API_KEY, api_key)
                        .build();

                URL url = new URL(buildUri.toString());
                Log.v(LOG_TAG, "BUILD URI : " + buildUri.toString());

                // Create the request to OpenWeatherMap, and open the connection

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
                Log.v(LOG_TAG, "Forecast Json String : " + forecastJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
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
             return getMovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<MovieData> results) {

            if (results != null) {
                mDbAdapter.clear();
                mDbAdapter.addAll(results);
            }else {
                Toast.makeText(getActivity(), "could not connect to network", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
