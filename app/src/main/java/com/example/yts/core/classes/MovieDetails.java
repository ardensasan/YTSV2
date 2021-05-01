package com.example.yts.core.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yts.R;
import com.example.yts.torrent.Torrent;
import com.example.yts.torrent.TorrentDownloadsList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetails {
    private String movieType = null;
    private String movieSynopsis = null;
    private String movieTitle = null;
    private ArrayList<String> movieRatingImages = new ArrayList<>();
    private ArrayList<String> movieRatings = new ArrayList<>();
    private ArrayList<String> movieQuality = new ArrayList<>();
    private ArrayList<String> magnetLinks = new ArrayList<>();
    private boolean isDoneFetching = false;
    private boolean isDisplayed = false;

    public MovieDetails(String movieTitle){
        this.movieTitle = movieTitle;
    }
    public void fetchMovieDetails(String movieURL){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(movieURL).get();

                    //get movie info
                    Element content = doc.getElementById("mobile-movie-info");
                    Elements elements = content.select("h1, h2");
                    int i = 0;
                    for(Element element:elements){
                        builder.append(element.text());
                        if(i != elements.size()-1){
                            builder.append("\n");
                        }
                        i++;
                    }
                    movieType = builder.toString();

                    //get movie ratings
                    elements = doc.getElementsByClass("rating-row");
                    for(Element element:elements){
                        movieRatings.add(element.getElementsByTag("span").text());
                        movieRatingImages.add(element.getElementsByTag("img").attr("abs:src"));
                    }

                    //get movie synopsis
                    content = doc.getElementById("synopsis");
                    movieSynopsis = content.getElementsByClass("hidden-sm hidden-md hidden-lg").text();

                    //get movie quality
                    content = doc.getElementById("movie-content");
                    elements = content.getElementsByClass("modal-quality");
                    builder.setLength(0);
                    for(Element element:elements){
                        movieQuality.add(element.text());
                    }
                    elements = content.getElementsByClass("quality-size");
                    int count = 0;
                    int index = 0;
                    for(Element element:elements){
                        String string = movieQuality.get(index);
                        movieQuality.set(index, string + " " + element.text()+"   ");
                        if (count % 2 != 0) {
                            index++;
                        }
                        count++;
                    }
                    //get magnet links
                    elements = content.getElementsByClass("magnet-download download-torrent magnet");
                    for(Element element:elements){
                        magnetLinks.add(element.attr("href"));
                    }
                    isDoneFetching = true;
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
            }
        }).start();
        return;
    }

    public boolean getIsDoneFetching(){
        return isDoneFetching;
    }

    public void displayMovieDetails(LinearLayout llv_movie_details, LinearLayout llh_movie_details, Activity activity){
        if(!isDisplayed) {
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            //display movie title
            TextView textView = new TextView(activity);
            textView.setText(movieType);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(Gravity.LEFT);
            params.setMargins(10,10,0,0);
            textView.setLayoutParams(params);
            linearLayout.addView(textView);


            //display ratings
            for (int i = 1; i < movieRatings.size(); i++) {
                LinearLayout ll = new LinearLayout(activity);
                ll.setOrientation(LinearLayout.HORIZONTAL);

                //rating logo
                ImageView imageView = new ImageView(activity);
                if(i == 1){
                    imageView.setImageResource(R.drawable.ic_movie_likes_24dp);
                }else if(i == movieRatings.size()-1){
                    imageView.setImageResource(R.drawable.ic_logo_imdb);
                }else{
                    loadImage(activity, imageView, movieRatingImages.get(i-1));
                }
                params = new LinearLayout.LayoutParams(50,50);
                params.setMargins(10,0,10,0);
                imageView.setLayoutParams(params);
                ll.addView(imageView);

                //rating text
                textView = new TextView(activity);
                textView.setText(movieRatings.get(i-1));
                ll.addView(textView);

                linearLayout.addView(ll);
            }
            //add image to image views
            for (int i = 1; i < movieRatings.size(); i++) {
            }
            llh_movie_details.addView(linearLayout);

            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,0,20);
            textView = new TextView(activity);
            textView.setText(movieSynopsis);
            textView.setLayoutParams(params);
            llv_movie_details.addView(textView);
            displayDownloadLinks(llv_movie_details, activity);
            isDisplayed = true;
        }
        return;
    }

    public boolean getIsDisplayed(){
        return isDisplayed;
    }

    private void displayDownloadLinks(LinearLayout llv_movie_details, Activity activity){
        //add layout element
        LinearLayout linearLayout;
        TextView textView;
        Button button;
        textView = new TextView(activity);
        textView.setText("DOWNLOADS");
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        llv_movie_details.addView(textView);
        for(int i = 0;i<movieQuality.size();i++){
            linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            textView = new TextView(activity);
            textView.setText(movieQuality.get(i));
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);

            //add magnet link button
            button = new Button(activity);
            button.setText("DOWNLOAD");
            button.setGravity(Gravity.CENTER);
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check if torrent already present
                    ((TorrentDownloadsList) activity.getApplication()).checkTorrentAlreadyAdded(magnetLinks.get(finalI), activity);
                }
            });
            linearLayout.addView(button);
            llv_movie_details.addView(linearLayout);
        }
    }

    private void loadImage(Activity activity,ImageView imageView, String imageURL){
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                Bitmap bitmap = null;
                URL url = null;
                try {
                    url = new URL(imageURL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap finalBitmap = bitmap;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBitmap);
                    }
                });
            }
        };
        thread.start();
        return;
    }
}
