package com.example.yts.ui.movie_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.core.classes.MovieDetails;

public class MovieDetailsViewModel extends ViewModel {
    private MutableLiveData<MovieDetails> movieDetails;
    private MovieDetails mDetails;

    public MovieDetailsViewModel() {
        movieDetails = new MutableLiveData<>();
    }

    public LiveData<MovieDetails> getMovieDetails() {
        return movieDetails;
    }

    public void fetchMovieDetails(String movieTitle, String movieURL){
        mDetails = new MovieDetails(movieTitle);
        mDetails.fetchMovieDetails(movieURL);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!mDetails.getIsDoneFetching()){
                    try {
                        Thread.sleep(100);
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