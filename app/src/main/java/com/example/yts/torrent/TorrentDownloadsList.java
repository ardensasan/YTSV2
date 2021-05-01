package com.example.yts.torrent;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class TorrentDownloadsList extends Application {
    private static ArrayList<Torrent> torrentDownloadsList = new ArrayList<>();

    public ArrayList<Torrent> getTorrentDownloadsList(){
        return torrentDownloadsList;
    }

    public void addTorrentToList(Torrent torrent){
        torrentDownloadsList.add(torrent);
        torrent.downloadStart();
    }

    public Torrent getTorrent(Integer position){
        return torrentDownloadsList.get(position);
    }

    public Integer getSize(){
        return torrentDownloadsList.size();
    }

    //check if torrent is already added
    public void checkTorrentAlreadyAdded(String magnetURI, Activity activity){
        boolean isAlreadyAdded = false;
        String torrentName = "";
        for(Torrent torrent:torrentDownloadsList){
            if(torrent.getMagnetURI() == magnetURI){
                isAlreadyAdded = true;
                torrentName = torrent.getTorrentName();
            }
        }
        if(!isAlreadyAdded){
            Torrent torrent = new Torrent(magnetURI);
            ((TorrentDownloadsList) activity.getApplication()).addTorrentToList(torrent);
            Toast.makeText(activity, "Added to download queue", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(activity, torrentName +" already in download queue", Toast.LENGTH_SHORT).show();
        }
    }

    //remove torrent from list
    public void removeTorrent(Torrent torrent){
        torrent.removeTorrent();
        torrentDownloadsList.remove(torrent);
    }
}