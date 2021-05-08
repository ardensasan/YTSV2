package com.example.yts.torrent;

//start new thread with a "new Thread(new demoRunnable()).start()" call

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.frostwire.jlibtorrent.*;
import com.frostwire.jlibtorrent.alerts.*;

public class Torrent extends Thread implements Serializable {
    private String magnetURI;
    private SessionManager sessionManager = null;
    private TorrentInfo torrentInfo = null;
    private TorrentHandle torrentHandle = null;
    private String torrentName = "Getting metadata...";
    private boolean isPaused = false; //torrent is paused
    private boolean isTimedOut = false;
    private boolean isSelected = false; //torrent is select in recyclerview
    private boolean isRemoved = false;
    private File torrentFolder;

    public Torrent(String magnetURI){
        this.magnetURI = magnetURI;
    }

    public String getMagnetURI(){
        return magnetURI;
    }

    public void downloadStart(){
        torrentName = "Getting metadata...";
        Thread thread = new Thread(() -> {
            if(sessionManager == null){
                sessionManager = new SessionManager();
            }
            try {
                startdl();
            } catch (InterruptedException e) {
            }
        });
        thread.start();
    }

    private boolean waitForNodesInDHT(SessionManager s) throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long nodes = s.stats().dhtNodes();
                if (nodes >= 10) {
                    log("DHT contains " + nodes + " nodes");
                    signal.countDown();
                    timer.cancel();
                }
            }
        }, 0, 1000);

        log("Waiting for nodes in DHT (10 seconds)...");
        boolean r = signal.await(10, TimeUnit.SECONDS);
        if (!r) {
            log("DHT bootstrap timeout");
        }
        return r;
    }

    private static void log(String s) {
        Log.d("status",s);
    }

    private void startdl() throws InterruptedException {
        String link = magnetURI;
        File saveDir = new File(Environment.getExternalStorageDirectory()+"/Download/");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        AlertListener l = new AlertListener() {
            private int grade = 0;
            @Override
            public int[] types() {
                return null;
            }

            @Override
            public void alert(Alert<?> alert) {
                AlertType type = alert.type();
                switch (type) {
                    case ADD_TORRENT:
                        torrentHandle = ((AddTorrentAlert) alert).handle();
                        ((AddTorrentAlert) alert).handle().resume();
                        ((AddTorrentAlert) alert).handle();
                        break;
                    case TORRENT_FINISHED:
                        ((TorrentFinishedAlert) alert).handle().pause();
                        break;
                    default:
                        break;
                }
            }
        };
        sessionManager.addListener(l);
        if (!sessionManager.isRunning())
            sessionManager.start();
        if (link.startsWith("magnet:?")) {
            boolean bool = waitForNodesInDHT(sessionManager);
            if(!bool){
                torrentName = "DHT bootstrap timeout Please retry download";
                isTimedOut = true;
                isPaused = true;
                return;
            }else{
                byte[] data = sessionManager.fetchMagnet(link, 30);
                if(data == null){
                    torrentName = "Download timeout please retry";
                    isTimedOut = true;
                    isPaused = true;
                    return;
                }
                torrentInfo = TorrentInfo.bdecode(data);
                sessionManager.download(torrentInfo, saveDir);
                torrentName = torrentInfo.name();
                torrentFolder = new File(Environment.getExternalStorageDirectory()+"/Download/"+torrentName);
                torrentHandle = sessionManager.find(torrentInfo.infoHash());
            }
        }
    }


    public int getProgress(){
        if(torrentInfo !=null){
            return (int) (sessionManager.find(torrentInfo.infoHash()).status().progress() * 100);
        }else{
            return 0;
        }
    }

    public String getTotalDownload(){
        if(torrentInfo !=null) {
            float totalDone = sessionManager.find(torrentInfo.infoHash()).status().totalDone()/1000;
            String sTotalDone;
            if(totalDone >= 1000000){
                totalDone /= 1000000;
                sTotalDone = (Math.round(totalDone * 100.0) / 100.0) + " GB";
            }else if(totalDone >= 1000){
                totalDone /= 1000;
                sTotalDone = (Math.round(totalDone * 100.0) / 100.0) + " MB";
            }else{
                sTotalDone = totalDone + " KB";
            }

            float totalSize = sessionManager.find(torrentInfo.infoHash()).status().total()/1000;
            String sTotalSize;
            if(totalSize >= 1000000){
                totalSize /= 1000000;
                sTotalSize = (Math.round(totalSize * 100.0) / 100.0) + " GB";
            }else if(totalSize >= 1000){
                totalSize /= 1000;
                sTotalSize = (Math.round(totalSize * 100.0) / 100.0) + " MB";
            }else if(totalSize == 0){
                sTotalSize = sTotalDone;
            }
            else{
                sTotalSize = totalSize + " KB";
            }
            return sTotalDone +" / "+ sTotalSize;

        }else {
            return "";
        }
    }

    public String getTorrentState(){
        if(torrentInfo !=null) {
            if(isPaused){
                return "PAUSED";
            }else{
                return String.valueOf(sessionManager.find(torrentInfo.infoHash()).status().state());
            }
        }else{
            return "";
        }
    }

    //get speed based on torrent state
    //seeding = upload speed
    //download = download speed
    public String getSpeed(){
        if(torrentHandle !=null) {
            if(isPaused){
                return "--";
            }else {
                if(torrentHandle.status().isSeeding()){
                    float uploadSpeed = sessionManager.find(torrentInfo.infoHash()).status().uploadRate()/1000;
                    String sUploadSpeed ;
                    if(uploadSpeed >= 1000000){
                        uploadSpeed /= 1000000;
                        sUploadSpeed = (Math.round(uploadSpeed * 100.0) / 100.0) + " GB/s";
                    }else if(uploadSpeed >= 1000){
                        uploadSpeed /= 1000;
                        sUploadSpeed = (Math.round(uploadSpeed * 100.0) / 100.0) + " MB/s";
                    }else{
                        sUploadSpeed = uploadSpeed + " KB/s";
                    }
                    return sUploadSpeed;
                }else{
                    float downloadSpeed = sessionManager.find(torrentInfo.infoHash()).status().downloadRate()/1000;
                    String sDownloadSpeed;
                    if(downloadSpeed >= 1000000){
                        downloadSpeed /= 1000000;
                        sDownloadSpeed = (Math.round(downloadSpeed * 100.0) / 100.0) + " GB/s";
                    }else if(downloadSpeed >= 1000){
                        downloadSpeed /= 1000;
                        sDownloadSpeed = (Math.round(downloadSpeed * 100.0) / 100.0) + " MB/s";
                    }else{
                        sDownloadSpeed = downloadSpeed + " KB/s";
                    }
                    return sDownloadSpeed;
                }
            }
        }else{
            return "";
        }
    }

    public String getTorrentName(){
        return torrentName;
    }

    public void pauseTorrent(){
        if(torrentHandle != null){
            torrentHandle.pause();
            isPaused = true;
        }
    }

    public boolean getIsPaused(){
        return isPaused;
    }

    public void resumeTorrent(){
        if(isTimedOut){
            isTimedOut = false;
            downloadStart();
        }else{
            torrentHandle.resume();
        }
        isPaused = false;
    }

    public boolean infoLoaded(){
        if(torrentInfo != null){
            return true;
        }else{
            return false;
        }
    }

    public void setIsSelected(boolean bool){
        isSelected = bool;
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public void removeTorrent(boolean deleteFolder){
        new Thread(() -> sessionManager.stop()).start();
        if(deleteFolder){
            if (torrentFolder.isDirectory())
            {
                String[] children = torrentFolder.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(torrentFolder, children[i]).delete();
                }
                torrentFolder.delete();
            }
        }
    }
}