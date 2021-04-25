package com.example.yts.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.AddMovieToLayout;
import com.example.yts.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fetchWebsite();
    }

    private void fetchWebsite(){
        ArrayList<String> popularDownloadsMoviePoster = new ArrayList<>();
        ArrayList<String> popularDownloadsLink = new ArrayList<>();
        ArrayList<String> popularDownloadsTitle = new ArrayList<>();
        ArrayList<String> popularDownloadsYear = new ArrayList<>();

        ArrayList<String> latestMoviesMoviePoster = new ArrayList<>();
        ArrayList<String> latestMoviesLink = new ArrayList<>();
        ArrayList<String> latestMoviesTitle = new ArrayList<>();
        ArrayList<String> latestMoviesYear = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(getString(R.string.yts_website)).get();
                    String title = doc.title();
                    //get popular downloads
                    Element content = doc.getElementById("popular-downloads");
                    Elements elements = content.getElementsByClass("browse-movie-wrap col-xs-10 col-sm-5");

                    for (Element element : elements) {
                        popularDownloadsMoviePoster.add(element.getElementsByTag("img").attr("abs:src"));
                        popularDownloadsLink.add(element.getElementsByTag("a").attr("href"));
                        popularDownloadsTitle.add(element.getElementsByClass("browse-movie-title").text());
                        popularDownloadsYear.add(element.getElementsByClass("browse-movie-year").text());
                    }


                    //save cover image link to array
                    content = doc.select("div.home-movies").first();
                    elements = content.getElementsByClass("browse-movie-wrap col-xs-10 col-sm-5");
                    for (Element element : elements) {
                        latestMoviesMoviePoster.add(element.getElementsByTag("img").attr("abs:src"));
                        latestMoviesLink.add(element.getElementsByTag("a").attr("href"));
                        latestMoviesTitle.add(element.getElementsByClass("browse-movie-title").text());
                        latestMoviesYear.add(element.getElementsByClass("browse-movie-year").text());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("n");
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayPopularDownloads(popularDownloadsMoviePoster, popularDownloadsLink, popularDownloadsTitle, popularDownloadsYear, getActivity());
                        displayLatestMovies(latestMoviesMoviePoster, latestMoviesLink, latestMoviesTitle, latestMoviesYear, getActivity());
                    }
                });
            }
        }).start();
        return;
    }

    //display popular downloads
    private void displayPopularDownloads(ArrayList<String> moviePosters, ArrayList<String> movieLinks, ArrayList<String> movieTitles, ArrayList<String> movieYears, FragmentActivity activity){
        LinearLayout llv_movies_home = getView().findViewById(R.id.llv_movies_home);
        new AddMovieToLayout("Popular Downloads", llv_movies_home, moviePosters,movieLinks,movieTitles,movieYears,activity);
        return;
    }

    //display latest movies
    private void displayLatestMovies(ArrayList<String> moviePosters, ArrayList<String> movieLinks, ArrayList<String> movieTitles, ArrayList<String> movieYears, FragmentActivity activity){
        LinearLayout llv_movies_home = getView().findViewById(R.id.llv_movies_home);
        new AddMovieToLayout("Latest Movies",llv_movies_home, moviePosters,movieLinks,movieTitles,movieYears,activity);
        return;
    }

}