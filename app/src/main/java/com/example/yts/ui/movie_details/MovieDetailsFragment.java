package com.example.yts.ui.movie_details;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.core.classes.Movie;
import com.example.yts.core.classes.MovieDetails;
import com.example.yts.R;

public class MovieDetailsFragment extends Fragment {
    private Movie movie;
    public MovieDetailsFragment(Movie movie) {
        this.movie = movie;
    }
    private static MovieDetailsViewModel movieDetailsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        final TextView textView = root.findViewById(R.id.tv_movie_details_fragment);
        textView.setText("Fetching Movie Details...");

        LinearLayout llv_movie_details = root.findViewById(R.id.llv_movie_details);
        LinearLayout llh_movie_details = root.findViewById(R.id.llh_movie_details);
        //get movie class parameter
        Activity activity = getActivity();
        movieDetailsViewModel = new ViewModelProvider(this).get(MovieDetailsViewModel.class);
        movieDetailsViewModel.fetchMovieDetails(movie.getMovieTitle(), movie.getMovieURL());
        //display movie details and movie poster
        movieDetailsViewModel.getMovieDetails().observe(getViewLifecycleOwner(), movieDetails -> {
            textView.setText("");
            ImageView imageView = root.findViewById(R.id.iv_movie_poster);
            movie.displayMoviePoster(imageView,activity);
            int imageWidth = (Resources.getSystem().getDisplayMetrics().widthPixels / 3) - 20;
            int imageHeight = imageWidth * activity.getResources().getInteger(R.integer.movie_poster_height) / activity.getResources().getInteger(R.integer.movie_poster_width);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            movieDetails.displayMovieDetails(llv_movie_details, llh_movie_details, activity);
        });
        return root;
    }
}