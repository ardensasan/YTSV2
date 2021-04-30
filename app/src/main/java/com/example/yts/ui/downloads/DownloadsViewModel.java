package com.example.yts.ui.downloads;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yts.torrent.Torrent;
import com.example.yts.torrent.TorrentDownloadsList;

import java.util.ArrayList;

public class DownloadsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Torrent>> torrents;
    public DownloadsViewModel() {
        torrents = new MutableLiveData<>();
    }

    public LiveData<ArrayList<Torrent>> getTorrentDownloads() {
        return torrents;
    }

    public void setTorrents(Activity activity) {
        torrents.postValue(((TorrentDownloadsList) activity.getApplication()).getTorrentDownloadsList());
    }
}