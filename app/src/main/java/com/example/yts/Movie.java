package com.example.yts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Movie {
    private String moviePosterURL;
    private String movieURL;
    private String movieTitle;
    private String movieYear;

    public Movie(String moviePosterURL, String movieURL, String movieTitle, String movieYear){
        this.movieURL = movieURL;
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.moviePosterURL = moviePosterURL;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public String getMovieURL(){return movieURL;}

    public String getMovieYear(){return movieYear;}

    public String getMoviePosterURL(){return moviePosterURL;}
}
