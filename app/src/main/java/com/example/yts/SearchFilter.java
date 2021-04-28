package com.example.yts;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SearchFilter {
    private String filterName;
    private ArrayList<String> filterItems  = new ArrayList<>();
    private ArrayList<String> filterItemsValue = new ArrayList<>();

    public SearchFilter(String filterName, Element content, String filterCSSQuery){
        this.filterName = filterName;
        Elements elements = content.select("select[name=\""+filterCSSQuery+"\"]").select("select option");
        for(Element element:elements){
            filterItems.add(element.text());
            filterItemsValue.add(element.attr("value"));
        }
    }

    public String getFilterName(){
        return filterName;
    }

    public ArrayList<String> getFilterItems(){
        return filterItems;
    }
}
