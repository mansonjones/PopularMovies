package com.example.android.popularmovies2.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        // The detail Activity called via intent.
        Intent intent = getActivity().getIntent();
        if (intent != null) {

            if (intent.hasExtra("MOVIE_TITLE")) {
                String movieTitle = intent.getStringExtra("MOVIE_TITLE");
                ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieTitle);
            }

            if (intent.hasExtra("MOVIE_USER_RATING")) {
                String userRating = intent.getStringExtra("MOVIE_USER_RATING");
                ((TextView) rootView.findViewById(R.id.movie_vote_average)).setText(userRating);
            }

            if (intent.hasExtra("MOVIE_PLOT_SYNOPSIS")) {
                String plotSynopsis = intent.getStringExtra("MOVIE_PLOT_SYNOPSIS");
                ((TextView) rootView.findViewById(R.id.movie_plot_synopsis)).setText(plotSynopsis);
            }

            if (intent.hasExtra("MOVIE_RELEASE_DATE")) {
                String releaseDate = intent.getStringExtra("MOVIE_RELEASE_DATE");
                ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(releaseDate);
            }

            if (intent.hasExtra("MOVIE_POSTER")) {
                String posterPath = intent.getStringExtra("MOVIE_POSTER");
                String fullPosterPath = "http://image.tmdb.org/t/p/w185" + posterPath;
                ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_thumbnail));
                Picasso.with(getActivity())
                        .load(fullPosterPath)
                                // .load("http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg")
                        .placeholder(R.drawable.sample_0)
                        .resize(100, 185)
                        .centerCrop()
                        .into(imageView);
            }
        }
        return rootView;
    }
}
