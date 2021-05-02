package com.example.yts.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.core.display.DisplayMovies;
import com.example.yts.core.classes.Movie;
import com.example.yts.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static HomeViewModel homeViewModel = null;
    private DisplayMovies displayMovies;
    private FragmentManager fragmentManager;

    public HomeFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.tv_home_message);
        textView.setText("Fetching Movie List...");
        LinearLayout llv_movies_home = root.findViewById(R.id.llv_movies_home);
        if(homeViewModel == null){
            homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            homeViewModel.fetchMovies(getString(R.string.yts_website), "popular-downloads", "browse-movie-wrap col-xs-10 col-sm-5");
            homeViewModel.fetchMovies(getString(R.string.yts_website), "div.home-movies", "browse-movie-wrap col-xs-10 col-sm-5");
        }
        homeViewModel.getPopularMoviesList().observe(getViewLifecycleOwner(), movies -> {
            if(movies.size() > 0){
                textView.setText("");
                displayMovies = new DisplayMovies(movies);
                displayMovies.display("Popular Downloads", llv_movies_home,getActivity(),fragmentManager);
            }
        });

        homeViewModel.getLatestMoviesList().observe(getViewLifecycleOwner(), movies -> {
            if(movies.size() > 0){
                displayMovies = new DisplayMovies(movies);
                displayMovies.display("Latest Movies", llv_movies_home,getActivity(),fragmentManager);
            }
        });
        return root;
    }
}