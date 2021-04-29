package com.example.yts.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.core.classes.Movie;
import com.example.yts.core.fetcher.MovieFetcher;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> popularMoviesList;
    private MutableLiveData<ArrayList<Movie>> latestMoviesList;

    public HomeViewModel() {
        popularMoviesList = new MutableLiveData<>();
        latestMoviesList = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Movie>> getPopularMoviesList() {
        return popularMoviesList;
    }

    public LiveData<ArrayList<Movie>> getLatestMoviesList() {
        return latestMoviesList;
    }

    public void fetchMovies(String url, String elementID, String elementClass){
        MovieFetcher movieFetcher = new MovieFetcher(url,elementID,elementClass);
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
                if(movieFetcher.getIsDoneFetching()){
                    if(elementID == "popular-downloads"){
                        popularMoviesList.postValue(movieFetcher.getFetchedMovies());
                    }else if(elementID == "div.home-movies"){
                        latestMoviesList.postValue(movieFetcher.getFetchedMovies());
                    }
                }
            }
        });
        thread.start();
        return;
    }
}