package com.example.yts.torrent;

import android.app.Activity;
import android.app.Application;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

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
            if(torrent.getMagnetURI().equals(magnetURI)){
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
    public void removeTorrent(Torrent torrent,boolean deleteFolder){
        torrent.removeTorrent(deleteFolder);
        torrentDownloadsList.remove(torrent);
    }

    public boolean getHasSelectedItem(){
        for(Torrent torrent: torrentDownloadsList){
            if(torrent.getIsSelected()){
                return true;
            }
        }
        return false;
    }

    public void resetSelectedItems(){
        for(Torrent torrent: torrentDownloadsList){
            torrent.setIsSelected(false);
        }
    }

    public void selectAllTorrents(){
        for(Torrent torrent: torrentDownloadsList){
            torrent.setIsSelected(true);
        }
    }

    public void selectedItemClearDelete(boolean delete){
        for (Torrent torrent: torrentDownloadsList){
            if(torrent.getIsSelected()){
                torrent.pauseTorrent();
                torrent.removeTorrent(delete);
            }
        }
        Iterator<Torrent> iterator = torrentDownloadsList.iterator();
        while (iterator.hasNext()){
            Torrent torrent = iterator.next();
            if(torrent.getIsSelected()){
                iterator.remove();
            }
        }
    }
}