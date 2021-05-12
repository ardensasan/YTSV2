package com.example.yts.core.classes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//movie class as serializable for easier data passing from one fragment to another
public class Movie{
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        return;
    }

    //display movie poster in image button
    public void displayMoviePoster(ImageButton imageButton, Activity activity){
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                int counter = 0;
                while(!getIsDoneLoading()) {
                    try {
                        counter++;
                        Thread.sleep(100);
                        if(counter > 100){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                activity.runOnUiThread(() -> imageButton.setImageBitmap(getMoviePoster()));
            }
        };
        thread.start();
    }

    //display movie poster in image view
    public void displayMoviePoster(ImageView imageView, Activity activity){
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                int counter = 0;
                while(!getIsDoneLoading()) {
                    try {
                        counter++;
                        Thread.sleep(100);
                        if(counter > 100){
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                activity.runOnUiThread(() -> imageView.setImageBitmap(getMoviePoster()));
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
