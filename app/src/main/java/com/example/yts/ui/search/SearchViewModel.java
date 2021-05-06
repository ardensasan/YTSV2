package com.example.yts.ui.search;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.core.classes.Movie;
import com.example.yts.core.fetcher.MovieFetcher;
import com.example.yts.core.classes.SearchFilter;
import com.example.yts.core.fetcher.SearchFilterFetcher;

import java.io.IOException;
import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> movieList;
    private MutableLiveData<ArrayList<SearchFilter>> searchFilters;
    private MovieFetcher movieFetcher;
    private SearchFilterFetcher searchFilterFetcher;
    private boolean isInterrupted = false;

    public SearchViewModel() {
        movieList = new MutableLiveData<>();
        searchFilters = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movieList;
    }

    public LiveData<ArrayList<SearchFilter>> getSearchFilters() {
        return searchFilters;
    }


    public void fetchMovies(String url, String elementClass){
        movieFetcher = new MovieFetcher(url,elementClass);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!movieFetcher.getIsDoneFetching()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                movieList.postValue(movieFetcher.getFetchedMovies());
            }
        });
        thread.start();
        return;
    }

    public void fetchSearchFilters(String url) throws IOException {
        isInterrupted = false;
        searchFilterFetcher = new SearchFilterFetcher(url);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!searchFilterFetcher.getIsDoneFetching()){
                    if(isInterrupted){
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                searchFilterFetcher.getSearchFilters();
            }
        });
        thread.start();
        return;
    }
    public String getResultNum(){
        return movieFetcher.getResultNumString();
    }

    public Integer getTotalPages(){
        return movieFetcher.getTotalPages();
    }

    public void setInterrupted(boolean bool){
        isInterrupted = bool;
    }
}