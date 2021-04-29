package com.example.yts.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.Movie;
import com.example.yts.MovieDetails;
import com.example.yts.MovieFetcher;
import com.example.yts.SearchFilter;
import com.example.yts.SearchFilterFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MovieDetailsViewModel extends ViewModel {
    private MutableLiveData<MovieDetails> movieDetails;
    private MovieDetails mDetails;

    public MovieDetailsViewModel() {
        movieDetails = new MutableLiveData<>();
    }

    public LiveData<MovieDetails> getMovieDetails() {
        return movieDetails;
    }

    public void fetchMovieDetails(String movieURL){
        mDetails = new MovieDetails();
        mDetails.fetchMovieDetails(movieURL);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!mDetails.getIsDoneFetching()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                movieDetails.postValue(mDetails);
            }
        });
        thread.start();
        return;
    }
}