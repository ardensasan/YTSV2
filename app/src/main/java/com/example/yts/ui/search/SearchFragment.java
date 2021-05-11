package com.example.yts.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.core.display.DisplayFilters;
import com.example.yts.core.display.DisplayMovies;
import com.example.yts.core.display.DisplayPagination;
import com.example.yts.core.classes.Movie;
import com.example.yts.R;
import com.example.yts.core.classes.SearchFilter;
import com.example.yts.core.fetcher.SearchFilterFetcher;

import java.io.IOException;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static SearchViewModel searchViewModel = null;
    private DisplayMovies displayMovies;
    private static DisplayFilters displayFilters = null;
    private static DisplayPagination displayPagination;
    private static SearchFilterFetcher searchFilterFetcher = null;
    private FragmentManager fragmentManager;
    private static String query = "0";
    private Integer currentPage = 1;

    public SearchFragment(FragmentManager fragmentManager, SearchFilterFetcher searchFilterFetcher) {
        this.searchFilterFetcher = searchFilterFetcher;
        this.fragmentManager = fragmentManager;
    }

    public SearchFragment(FragmentManager fragmentManager, String query, Integer page){
        this.fragmentManager = fragmentManager;
        this.query = query;
        this.currentPage = page;
        this.searchViewModel = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.text_search);
        LinearLayout llv_browse_movies = root.findViewById(R.id.llv_browse_movies);
        LinearLayout llh_movie_filters = root.findViewById(R.id.llh_movie_filters);
        LinearLayout llh_pagination = root.findViewById(R.id.llh_pagination);
        SearchView searchView = root.findViewById(R.id.sv_search_movies);
        Button btn_browse_movies = root.findViewById(R.id.btn_browse_movies);
        btn_browse_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewModel = null;
                searchFilterFetcher.setDefaultFilters();
                fragmentManager.beginTransaction().replace(R.id.fl_fragment_container,  new SearchFragment(fragmentManager, "0",1), "search").addToBackStack("search").commit();
            }
        });
        String queryText = "";
        if(query != "0"){
            queryText = query;
        }
        searchView.setQuery(queryText, false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()){
                    query = "0";
                }
                searchFilterFetcher.setDefaultFilters();
                fragmentManager.beginTransaction().replace(R.id.fl_fragment_container,  new SearchFragment(fragmentManager, query,1), "search").addToBackStack("search").commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        textView.setText("Fetching Movie List...");

        if(displayFilters == null){
            displayFilters = new DisplayFilters();
        }


        if(searchViewModel == null) {
            searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
            StringBuilder url = new StringBuilder();
            url.append(getString(R.string.yts_movies));
            if(query.equals("0") && !searchFilterFetcher.isDefaultFilters()){
                url.append("/").append(query).append(searchFilterFetcher.getCompleteSearchFilterURL());
            }else if(!query.equals("0")){
                url.append("/").append(query).append(searchFilterFetcher.getCompleteSearchFilterURL());
            }
            if(currentPage != 1){
                url.append("?page=").append(currentPage);
            }
            searchViewModel.fetchMovies(String.valueOf(url), "browse-movie-wrap col-xs-10 col-sm-4 col-md-5 col-lg-4");
            displayPagination = new DisplayPagination();
        }
        searchViewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            textView.setText("");
            displayMovies = new DisplayMovies(movies);
            displayMovies.display(llv_browse_movies, getActivity(), searchViewModel.getResultNum(),fragmentManager);
            displayPagination.display(query, currentPage, searchViewModel.getTotalPages(), llh_pagination, getActivity(), fragmentManager);
            displayFilters.displayFilters(searchFilterFetcher.getSearchFilters(), llh_movie_filters, getActivity(),fragmentManager, query);
        });
        return root;
    }
}