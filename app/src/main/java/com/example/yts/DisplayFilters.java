package com.example.yts;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayFilters {

    //load filters
    public DisplayFilters(ArrayList<SearchFilter> searchFilters, LinearLayout llh_movie_filters, Activity activity){
        LinearLayout linearLayout;
        for(SearchFilter searchFilter: searchFilters){
            linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(activity);
            textView.setText(searchFilter.getFilterName());
            linearLayout.addView(textView);
            Spinner spinner = new Spinner(activity);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, searchFilter.getFilterItems());
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            linearLayout.addView(spinner);
            llh_movie_filters.addView(linearLayout);
        }
        return;
    }
}
