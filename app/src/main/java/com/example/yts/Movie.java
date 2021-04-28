package com.example.yts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Movie {
    private Bitmap moviePoster = null;
    private boolean isDoneLoading = false;
    private String moviePosterURL;
    private String movieURL;
    private String movieTitle;
    private String movieYear;

    public Movie(String moviePosterURL, String movieURL, String movieTitle, String movieYear){
        this.movieURL = movieURL;
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.moviePosterURL = moviePosterURL;
        loadBitmap();
    }

    public void loadBitmap(){
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    URL url = new URL(moviePosterURL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    moviePoster = BitmapFactory.decodeStream(inputStream);
                    isDoneLoading =true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        return;
    }

    public String getMovieTitle(){
        return movieTitle;
    }

    public String getMovieURL(){return movieURL;}

    public String getMovieYear(){return movieYear;}

    public Bitmap getMoviePoster(){return moviePoster;}

    public boolean getIsDoneLoading(){
        return isDoneLoading;
    }
}
