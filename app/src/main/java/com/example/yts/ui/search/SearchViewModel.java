package com.example.yts.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.Movie;
import com.example.yts.MovieFetcher;
import com.example.yts.SearchFilter;
import com.example.yts.SearchFilterFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> movieList;
    private MutableLiveData<ArrayList<SearchFilter>> searchFilters;
    MovieFetcher movieFetcher;
    SearchFilterFetcher searchFilterFetcher;

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
        searchFilterFetcher = new SearchFilterFetcher(url);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!searchFilterFetcher.getIsDoneFetching()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                searchFilters.postValue(searchFilterFetcher.getSearchFilters());
            }
        });
        thread.start();
        return;
    }

    public String getResultNum(){
        return movieFetcher.getResultNum();
    }

    public Integer getTotalPages(){
        return movieFetcher.getTotalPages();
    }
}