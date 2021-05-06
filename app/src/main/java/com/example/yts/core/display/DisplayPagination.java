package com.example.yts.core.display;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yts.R;
import com.example.yts.ui.search.SearchFragment;
import com.example.yts.ui.search.SearchViewModel;

import java.io.IOException;

public class DisplayPagination {
    public void display(String query, Integer currentPage, Integer totalPages, LinearLayout linearLayout, Activity activity, FragmentManager fragmentManager){
        ImageButton imageButton;

        //display buttons if not on first page
        if(currentPage > 1){
            imageButton = new ImageButton(activity);
            imageButton.setImageResource(R.drawable.ic_baseline_first_page_24);
            //goto first page
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, new SearchFragment(fragmentManager, query,1), "search").addToBackStack("search").commit();
                }
            });
            linearLayout.addView(imageButton);

            imageButton = new ImageButton(activity);
            imageButton.setImageResource(R.drawable.ic_baseline_navigate_before_24);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, new SearchFragment(fragmentManager, query,currentPage-1), "search").addToBackStack("search").commit();
                }
            });
            linearLayout.addView(imageButton);
        }

        TextView textView = new TextView(activity);
        textView.setText("Page ");
        linearLayout.addView(textView);
        EditText editText = new EditText(activity);
        editText.setText(String.valueOf(currentPage));
        editText.setMaxLines(1);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //TODO change page
                    return true;
                }
                return false;
            }
        });
        linearLayout.addView(editText);

        textView = new TextView(activity);
        textView.setText("of "+totalPages);
        linearLayout.addView(textView);

        //display buttons if not on last page
        if(currentPage < totalPages){
            //next page
            imageButton = new ImageButton(activity);
            imageButton.setImageResource(R.drawable.ic_baseline_navigate_next_24);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, new SearchFragment(fragmentManager, query,currentPage+1), "search").addToBackStack("search").commit();
                }
            });
            linearLayout.addView(imageButton);

            //jump to last page
            imageButton = new ImageButton(activity);
            imageButton.setImageResource(R.drawable.ic_baseline_last_page_24);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, new SearchFragment(fragmentManager, query,totalPages), "search").addToBackStack("search").commit();
                }
            });
            linearLayout.addView(imageButton);
        }
    }
}
