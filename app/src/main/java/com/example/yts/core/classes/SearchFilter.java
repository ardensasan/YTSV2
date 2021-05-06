package com.example.yts.core.classes;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SearchFilter {
    private String filterName;
    private ArrayList<String> filterItems  = new ArrayList<>();
    private ArrayList<String> filterItemsValue = new ArrayList<>();
    private Integer filterPosition = 0;
    public boolean isDoneFetching;
    private Integer tempFilterPosition; //holds the position of filter selected before clicking the search submit button

    public SearchFilter(String filterName, Element content, String filterCSSQuery){
        isDoneFetching = false;
        this.filterName = filterName;
        if(filterCSSQuery == "language"){
            filterPosition = 2;
        }
        Log.d("", "SearchFilter: "+filterName);
        Elements elements = content.select("select[name=\""+filterCSSQuery+"\"]").select("select option");
        for(Element element:elements){
            filterItems.add(element.text());
            filterItemsValue.add(element.attr("value"));

        }
        Log.d("", "SearchFilter: "+filterItemsValue);
        isDoneFetching = true;
    }

    public void fetchSearchFilter(){

    }
    public String getFilterName(){
        return filterName;
    }

    public ArrayList<String> getFilterItems(){
        return filterItems;
    }

    public Integer getFilterPosition(){
        return filterPosition;
    }

    public void setTempFilterPosition(Integer tempFilterPosition){
        this.tempFilterPosition = tempFilterPosition;
    }

    public void finalizeFilterPosition(){
        filterPosition = tempFilterPosition;
    }

    public String getFilterValue(){
        Log.d("", "getFilterValue: "+filterItemsValue);
        if(!isDoneFetching){
            return "";
        }else{
            return filterItemsValue.get(filterPosition);
        }
    }

    public boolean getIsDoneFetching(){
        return isDoneFetching;
    }
    public String getFilterPositionValue(Integer filterPosition){
        return filterItemsValue.get(filterPosition);
    }
}
