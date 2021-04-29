package com.example.yts;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MovieFetcher {
    private boolean isDoneFetching = false;
    private ArrayList<Movie> movies = new ArrayList<>();
    private String resultNum = null;
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
                    resultNum = doc.select("h2").text();
                    for (Element element : elements) {
                        String moviePosterURL = element.getElementsByTag("img").attr("abs:src");
                        String movieURL = element.getElementsByTag("a").attr("href");
                        String movieTitle = element.getElementsByClass("browse-movie-title").text();
                        String movieYear = element.getElementsByClass("browse-movie-year").text();
                        Movie movie = new Movie(moviePosterURL, movieURL, movieTitle, movieYear);
                        movies.add(movie);
                    }
                    Element element = doc.getElementsByClass("tsc_pagination tsc_paginationA tsc_paginationA06").first();
                    element = element.getElementsByTag("a").last();
                    if(element != null){
                        String string = element.attr("abs:href");
                        totalPages = Integer.parseInt(string.substring(string.lastIndexOf("=") + 1));
                    }
                    isDoneFetching = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String getResultNum(){
        return resultNum;
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
