package com.example.android.popularmovies2.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mansonjones on 8/26/15.
 */
public class Movie implements Parcelable {

    // parcel keys
    private static final String KEY_TITLE = "original_title";       // returns a String
    private static final String KEY_POSTER_PATH = "poster_path";    // returns a String
    private static final String KEY_SYNOPSIS = "overview";          // returns a String
    private static final String KEY_USER_RATING = "vote_average";   // returns a Double
    private static final String KEY_RELEASE_DATE = "release_date";  // returns a String

    public String title;
    public String posterPath;
    public String synopsis;
    public String userRating;
    public String releaseDate;

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMovieDbRequestURL() {
        // Note: the poster path returned from the movieDb request
        // has a forward slash at the begining.
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    // Empty constructor for array creation

    public Movie() {
    }

    public Movie(String title,
                 String posterPath,
                 String synopsis,
                 String userRating,
                 String releaseDate) {
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public Movie(JSONObject jsonMovieObject) {
             try {
                this.title = jsonMovieObject.getString(KEY_TITLE);
                this.posterPath = jsonMovieObject.getString(KEY_POSTER_PATH);
                this.synopsis = jsonMovieObject.getString(KEY_SYNOPSIS);
                // To Do: Check on this.  It might be returned as a double,
                // In which case you would do getDouble and
                // String.valueOf(doubleValue);
                this.userRating = jsonMovieObject.getString(KEY_USER_RATING);
                this.releaseDate = jsonMovieObject.getString(KEY_RELEASE_DATE);
             } catch (JSONException e) {
                 e.printStackTrace();
                 this.title = "Error parsing the json object";
             }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // create a bundle for the key-value pairs

        Bundle bundle = new Bundle();

        // insert the key-value pairs to the bundle

        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_POSTER_PATH, posterPath);
        bundle.putString(KEY_SYNOPSIS, synopsis);
        bundle.putString(KEY_USER_RATING, userRating);
        bundle.putString(KEY_RELEASE_DATE, releaseDate);

        dest.writeBundle(bundle);

    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            // read the bundle containing the key-value pairs from the parcel
            Bundle bundle = in.readBundle();

            // instantiate a person using values from the bundle
            return new Movie(
                    bundle.getString(KEY_TITLE),
                    bundle.getString(KEY_POSTER_PATH),
                    bundle.getString(KEY_SYNOPSIS),
                    bundle.getString(KEY_USER_RATING),
                    bundle.getString(KEY_RELEASE_DATE));
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
