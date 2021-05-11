package com.example.yts.core.fetcher;

import android.util.Log;

import com.example.yts.core.classes.SearchFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

//fetches all search movie filters
public class SearchFilterFetcher {
    private ArrayList<SearchFilter> searchFilters = new ArrayList<>();
    private boolean isDoneFetching = false;
    private String[] filterDefaults = {"all", "all", "0", "latest", "0", "all"};
    public SearchFilterFetcher(String url) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchFilter searchFilter;
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    Element content = doc.getElementById("main-search");
                    //quality filter
                    searchFilter = new SearchFilter("Quality", content, "quality");
                    searchFilters.add(searchFilter);

                    //genre filter
                    searchFilter = new SearchFilter("Genre", content, "genre");
                    searchFilters.add(searchFilter);

                    //rating filter
                    searchFilter = new SearchFilter("Rating", content, "rating");
                    searchFilters.add(searchFilter);

                    //order by filter
                    searchFilter = new SearchFilter("Order by", content, "order_by");
                    searchFilters.add(searchFilter);

                    //year filter
                    searchFilter = new SearchFilter("Year", content, "year");
                    searchFilters.add(searchFilter);

                    //language filter
                    searchFilter = new SearchFilter("Language", content, "language");
                    searchFilters.add(searchFilter);

                    isDoneFetching = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public ArrayList<SearchFilter> getSearchFilters(){
        return searchFilters;
    }

    public void setDefaultFilters(){
        for(SearchFilter searchFilter: searchFilters){
            searchFilter.setDefaultFilterPosition();
        }
    }

    public String getCompleteSearchFilterURL(){
        boolean fetchFlag = false; //flag for filter if its done fetching or not
        StringBuilder stringBuilder = new StringBuilder();
        for(SearchFilter searchFilter: searchFilters){
            if(!searchFilter.isDoneFetching)
            {
                fetchFlag = true;
                break;
            }
            stringBuilder.append("/").append(searchFilter.getFilterValue());
        }
        if(fetchFlag){
            return "";
        }else{
            return String.valueOf(stringBuilder);
        }
    }

    public boolean getIsDoneFetching(){
        return isDoneFetching;
    }

    //check if filter are in default mode
    public boolean isDefaultFilters(){
        int i = 0;
        boolean isDefault = true;
        for(SearchFilter searchFilter: searchFilters){
            if(!filterDefaults[i].equals(searchFilter.getFilterPositionValue(searchFilter.getFilterPosition()))){
                isDefault = false;
            }
            i++;
        }
        return isDefault;
    }
}
