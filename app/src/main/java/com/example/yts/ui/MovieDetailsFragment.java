package com.example.yts.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yts.Movie;
import com.example.yts.MovieDetails;
import com.example.yts.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Movie movie = (Movie) getArguments().getSerializable("movie");
        LinearLayout llv_movie_details = root.findViewById(R.id.llv_movie_details);
        LinearLayout llh_movie_details = root.findViewById(R.id.llh_movie_details);

        Activity activity = getActivity();
        //display movie poster
        ImageView imageView = new ImageView(activity);
        movie.displayMoviePoster(imageView,activity);
        int imageWidth = (Resources.getSystem().getDisplayMetrics().widthPixels / 3) - 20;
        int imageHeight = imageWidth * activity.getResources().getInteger(R.integer.movie_poster_height) / activity.getResources().getInteger(R.integer.movie_poster_width);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        llh_movie_details.addView(imageView);

        Log.d("", "displayMovieDetails:"+activity);
//        movie.displayMovieDetails(activity,llv_movie_details,llh_movie_details);
        //display details
        MovieDetails movieDetails = new MovieDetails(activity);
        movieDetails.fetchMovieDetails(movie.getMovieURL(),llv_movie_details,llh_movie_details);
        return root;
    }
}