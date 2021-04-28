package com.example.yts.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.DisplayMovies;
import com.example.yts.Movie;
import com.example.yts.R;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static SearchViewModel searchViewModel = null;
    private DisplayMovies displayMovies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.text_search);
        LinearLayout llv_browse_movies = root.findViewById(R.id.llv_browse_movies);
        textView.setText("Fetching Movie List...");
        if(searchViewModel == null) {
            searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
            searchViewModel.fetchMovies(getString(R.string.yts_movies), "browse-movie-wrap col-xs-10 col-sm-4 col-md-5 col-lg-4");
        }
        searchViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies.size() > 0) {
                    textView.setText("");
                    displayMovies = new DisplayMovies(movies);
                    displayMovies.display("Popular Downloads", llv_browse_movies, getActivity());
                }
            }
        });
        return root;
    }
}