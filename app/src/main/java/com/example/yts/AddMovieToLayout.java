package com.example.yts;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AddMovieToLayout {
    //add movie to layout without category header
    public AddMovieToLayout(LinearLayout linearLayout,
                            ArrayList<String> moviePosters,
                            ArrayList<String> movieLinks,
                            ArrayList<String> movieTitles,
                            ArrayList<String> movieYears,
                            Activity activity) {
        LinearLayout ll;
        LinearLayout llh_browse_movies = new LinearLayout(activity);
        int imageWidth = (Resources.getSystem().getDisplayMetrics().widthPixels / 2) - 20;
        int imageHeight = imageWidth * activity.getResources().getInteger(R.integer.movie_poster_height) / activity.getResources().getInteger(R.integer.movie_poster_width);
        llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < moviePosters.size(); i++) {
            if (i % 2 == 0) {
                llh_browse_movies = new LinearLayout(activity);
                llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(llh_browse_movies);
            }
            ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            // add button view to vertical layout
            ImageButton imageButton = new ImageButton(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
            loadMoviePoster(imageButton,moviePosters.get(i),activity);
            //set onclick action
            int finalI = i;
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ll.addView(imageButton);

            // add text views to vertical layout
            TextView tv = new TextView(activity);
            tv.setText(movieTitles.get(i));
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);

            tv = new TextView(activity);
            tv.setText(movieYears.get(i));
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);
            llh_browse_movies.addView(ll);
        }
    }

    //add movie to layout with category header
    public AddMovieToLayout(String header, LinearLayout linearLayout,
                            ArrayList<String> moviePosters,
                            ArrayList<String> movieLinks,
                            ArrayList<String> movieTitles,
                            ArrayList<String> movieYears,
                            Activity activity) {
        LinearLayout ll;
        LinearLayout llh_browse_movies = new LinearLayout(activity);
        TextView textView = new TextView(activity);
        textView.setText(header);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(20);
        linearLayout.addView(textView);
        int imageWidth = (Resources.getSystem().getDisplayMetrics().widthPixels / 2) - 20;
        int imageHeight = imageWidth * activity.getResources().getInteger(R.integer.movie_poster_height) / activity.getResources().getInteger(R.integer.movie_poster_width);
        llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < moviePosters.size(); i++) {
            if (i % 2 == 0) {
                llh_browse_movies = new LinearLayout(activity);
                llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(llh_browse_movies);
            }
            ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            // add button view to vertical layout
            ImageButton imageButton = new ImageButton(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
            loadMoviePoster(imageButton, moviePosters.get(i), activity);
            //set onclick action
            int finalI = i;
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            ll.addView(imageButton);

            // add text views to vertical layout
            TextView tv = new TextView(activity);
            tv.setText(movieTitles.get(i));
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);

            tv = new TextView(activity);
            tv.setText(movieYears.get(i));
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);
            llh_browse_movies.addView(ll);
        }
    }

    private void loadMoviePoster(ImageButton imageButton, String imageURL, Activity activity){
//        Thread thread = new Thread() {
//            boolean isFinished = false;
//            @Override
//            public void run() {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            while (!isFinished){
//                                URL url = new URL(imageURL);
//                                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                                connection.setDoInput(true);
//                                connection.connect();
//                                InputStream input = connection.getInputStream();
//                                Bitmap bitmap = BitmapFactory.decodeStream(input);
//                                imageButton.setImageBitmap(bitmap);
//                                isFinished = true;
//                            }
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//            }
//        };
//        thread.start();
    }
}
