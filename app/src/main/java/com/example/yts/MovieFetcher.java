package com.example.yts;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MovieFetcher {
    private boolean isDoneFetching = false;
    private String url;
    private String elementID;
    private String elementClass;
    private ArrayList<Movie> fetchedMovies = new ArrayList<>();

    public MovieFetcher(String url, String elementID, String elementClass){
        this.url = url;
        this.elementID = elementID;
        this.elementClass = elementClass;
    }

    public void fetch(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
                    String title = doc.title();
                    Element content = null;
                    if(elementID == "div.home-movies"){
                        content = doc.select("div.home-movies").first();
                    }else if(elementID == "popular-downloads"){
                        content = doc.getElementById(elementID);
                    }
                    Elements elements = content.getElementsByClass(elementClass);

                    for (Element element : elements) {
                        String moviePosterURL = element.getElementsByTag("img").attr("abs:src");
                        String movieURL = element.getElementsByTag("a").attr("href");
                        String movieTitle = element.getElementsByClass("browse-movie-title").text();
                        String movieYear = element.getElementsByClass("browse-movie-year").text();
                        Movie movie = new Movie(moviePosterURL, movieURL, movieTitle, movieYear);
                        fetchedMovies.add(movie);
                    }
                    isDoneFetching = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return;
    }

    public ArrayList<Movie> getFetchedMovies(){
        return fetchedMovies;
    }

    public boolean getIsDoneFetching(){
        return isDoneFetching;
    }
}
