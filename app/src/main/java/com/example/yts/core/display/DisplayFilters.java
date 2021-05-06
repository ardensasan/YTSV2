package com.example.yts.core.display;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yts.core.classes.SearchFilter;

import java.util.ArrayList;

public class DisplayFilters {
    private ArrayList<Integer> filterPosition;

    //load filters
    public void displayFilters(ArrayList<SearchFilter> searchFilters,LinearLayout llh_movie_filters, Activity activity){
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
            spinner.setSelection(searchFilter.getFilterPosition());
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    searchFilter.setTempFilterPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            linearLayout.addView(spinner);
            llh_movie_filters.addView(linearLayout);
        }
        return;
    }
}
