package com.example.yts.ui.downloads;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yts.DownloadAdapter;
import com.example.yts.R;
import com.example.yts.core.classes.Movie;
import com.example.yts.core.classes.MovieDetails;
import com.example.yts.torrent.Torrent;
import com.example.yts.torrent.TorrentDownloadsList;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {

    private static DownloadsViewModel downloadsViewModel;
    private RecyclerView recyclerView;
    private static ArrayList<Torrent> torrentDownloadsList = null;
    private static boolean isOnFragment = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Activity activity = getActivity();
        if(downloadsViewModel == null){
            downloadsViewModel = new ViewModelProvider(this).get(DownloadsViewModel.class);

        }
        isOnFragment = true;
        View root = inflater.inflate(R.layout.fragment_downloads, container, false);
        TextView textView = root.findViewById(R.id.tv_downloads);
        textView.setText("Loading Downloads..");
        recyclerView = root.findViewById(R.id.rv_downloads);
        //update downloads list while on fragment
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isOnFragment){
                    try {
                        Thread.sleep(1000);
                        downloadsViewModel.setTorrents(activity);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        downloadsViewModel.getTorrentDownloads().observe(getViewLifecycleOwner(), new Observer<ArrayList<Torrent>>() {
            @Override
            public void onChanged(ArrayList<Torrent> torrents) {
                //get list of downloads and display in recycler view
                textView.setText("");
                torrentDownloadsList = ((TorrentDownloadsList) activity.getApplication()).getTorrentDownloadsList();
                DownloadAdapter downloadAdapter = new DownloadAdapter(torrentDownloadsList);
                recyclerView.setAdapter(downloadAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            }
        });
        return root;
    }

    public void onDestroyView () {
        super.onDestroyView();
        isOnFragment = false;
    }
}