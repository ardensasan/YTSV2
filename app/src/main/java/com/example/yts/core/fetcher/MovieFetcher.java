package com.example.yts.core.fetcher;

import com.example.yts.core.classes.Movie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class MovieFetcher {
    private boolean isDoneFetching = false;
    private ArrayList<Movie> movies = new ArrayList<>();
    private String resultNumString = null;
    private Integer resultNum;
    private Integer totalPages = 1;

    public MovieFetcher(String url, String elementID, String elementClass){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
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
                        movies.add(movie);
                    }
                    isDoneFetching = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //fetch movies in search result
    public MovieFetcher(String url, String elementClass){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements elements = doc.getElementsByClass(elementClass);
                    resultNumString = doc.select("h2").text();
                    NumberFormat format = NumberFormat.getInstance(Locale.US);
                    Number number = format.parse(doc.select("h2").select("b").text());
                    resultNum = number.intValue();
                    for (Element element : elements) {
                        String moviePosterURL = element.getElementsByTag("img").attr("abs:src");
                        String movieURL = element.getElementsByTag("a").attr("href");
                        String movieTitle = element.getElementsByClass("browse-movie-title").text();
                        String movieYear = element.getElementsByClass("browse-movie-year").text();
                        Movie movie = new Movie(moviePosterURL, movieURL, movieTitle, movieYear);
                        movies.add(movie);
                    }
                    totalPages = (int)Math.ceil(resultNum/20.0);
                    isDoneFetching = true;
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String getResultNumString(){
        return resultNumString;
    }
    public ArrayList<Movie> getFetchedMovies(){
        return movies;
    }

    public boolean getIsDoneFetching(){
        return isDoneFetching;
    }

    public Integer getTotalPages(){
        return totalPages;
    }
}
