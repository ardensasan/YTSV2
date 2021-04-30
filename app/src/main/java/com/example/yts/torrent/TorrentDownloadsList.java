package com.example.yts.torrent;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

public class TorrentDownloadsList extends Application {
    private ArrayList<Torrent> torrentDownloadsList = new ArrayList<>();

    public ArrayList<Torrent> getTorrentDownloadsList(){
        return torrentDownloadsList;
    }

    public void addTorrentToList(Torrent torrent){
        torrent.downloadStart();
        torrentDownloadsList.add(torrent);
    }

    public Torrent getTorrent(Integer position){
        return torrentDownloadsList.get(position);
    }

    public Integer getSize(){
        return torrentDownloadsList.size();
    }
}