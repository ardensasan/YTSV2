package com.example.yts.core.display;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.yts.ui.movie_details.MovieDetailsFragment;
import com.example.yts.core.classes.Movie;
import com.example.yts.R;

import java.util.ArrayList;


public class DisplayMovies extends AppCompatActivity {
    private ArrayList<Movie> movies;


    public DisplayMovies(ArrayList<Movie> movies){
        this.movies = movies;
    }

    //display movies along with header category
    public void display(String header, LinearLayout linearLayout, Activity activity, FragmentManager fragmentManager) {
        TextView textView = new TextView(activity);
        textView.setText(header);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(header.equals("Latest Movies")){
            params.setMargins(10,80,10,10);
        }else{
            params.setMargins(10,0,10,10);
        }
        params.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(params);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(20);
        linearLayout.addView(textView);
        display(linearLayout,activity,fragmentManager);
    }


    public void display(LinearLayout linearLayout, Activity activity,String resultNum,FragmentManager fragmentManager) {
        TextView textView = new TextView(activity);
        textView.setText(resultNum);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setLayoutParams(params);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextSize(20);
        linearLayout.addView(textView);
        display(linearLayout,activity,fragmentManager);
    }

    public void display(LinearLayout linearLayout, Activity activity, FragmentManager fragmentManager){
        LinearLayout ll;
        LinearLayout llh_browse_movies = new LinearLayout(activity);
        int imageWidth = (Resources.getSystem().getDisplayMetrics().widthPixels / 2) - 20;
        int imageHeight = imageWidth * activity.getResources().getInteger(R.integer.movie_poster_height) / activity.getResources().getInteger(R.integer.movie_poster_width);
        llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
        int i = -1;
        for (Movie movie: movies) {
            i++;
            if (i % 2 == 0) {
                llh_browse_movies = new LinearLayout(activity);
                llh_browse_movies.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(llh_browse_movies);
            }
            ll = new LinearLayout(activity);
            ll.setOrientation(LinearLayout.VERTICAL);

            //add movie poster to layout
            ImageButton imageButton = new ImageButton(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            imageButton.setLayoutParams(params);
            imageButton.setScaleType(ImageView.ScaleType.FIT_XY);

            //set onclick action
            imageButton.setOnClickListener(v -> {
                //send movie class as parameters to movie details fragment
                fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, new MovieDetailsFragment(movie),"movie_details").addToBackStack("movie_details").commit();
            });
            ll.addView(imageButton);

            //display movie poster
            movie.displayMoviePoster(imageButton,activity);

            // add movie title to layout
            TextView tv = new TextView(activity);
            tv.setText(movie.getMovieTitle());
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);

            // add movie year to layout
            tv = new TextView(activity);
            tv.setText(movie.getMovieYear());
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            params = new LinearLayout.LayoutParams(imageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(params);
            ll.addView(tv);
            llh_browse_movies.addView(ll);
        }
    }
}
