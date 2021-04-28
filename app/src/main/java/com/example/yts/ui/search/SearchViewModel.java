package com.example.yts.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.Movie;
import com.example.yts.MovieFetcher;

import java.util.ArrayList;

public class SearchViewModel extends ViewModel {
    private boolean newPage = true;
    private MutableLiveData<ArrayList<Movie>> movieList;

    public SearchViewModel() {
        movieList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movieList;
    }

    public void fetchMovies(String url, String elementClass){
        MovieFetcher movieFetcher = new MovieFetcher(url,elementClass);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!movieFetcher.getIsDoneFetching()){
                    try {
                        Thread.sleep(100);
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
}