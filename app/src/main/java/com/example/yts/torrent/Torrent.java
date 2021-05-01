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
    private SessionManager s;
    private TorrentInfo torrentInfo = null;
    private TorrentHandle torrentHandle = null;
    private String torrentName = "Getting metadata...";
    private boolean isPaused = false; //torrent is paused
    private boolean isTimedOut = false;
    private boolean isSelected = false; //torrent is select in recyclerview
    private boolean isRemoved = false;

    public Torrent(String magnetURI){
        this.magnetURI = magnetURI;
    }

    public String getMagnetURI(){
        return magnetURI;
    }

    public void downloadStart(){
        torrentName = "Getting metadata...";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                s = new SessionManager();
                try {
                    startdl(magnetURI, s);
                } catch (InterruptedException e) {
                    return;
                }
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

    private void startdl(String magnetLink, SessionManager sm) throws InterruptedException {
        String link = magnetLink;
        File saveDir = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/Download/");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        final SessionManager s = sm;//storrent.getSessionManager();
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
                    case PIECE_FINISHED:
                        int progress = (int) (((PieceFinishedAlert) alert).handle().status().progress() * 100);
                        if (grade < progress / 20) {
                            int index = (int) (((PieceFinishedAlert) alert).pieceIndex());
                            log("index: " + index);
                            grade += 1;
                            s.downloadRate();
                            log(progress + " %  downloaded");
                        }
                        log("PIECE_FINISHED");
                        break;
                    case TORRENT_FINISHED:
                        grade = 0;
                        ((TorrentFinishedAlert) alert).handle().pause();
                        log("TORRENT_FINISHED");
                        break;
                    case TORRENT_ERROR:
                        log(((TorrentErrorAlert) alert).what());
                        log("is paused = " + ((TorrentErrorAlert) alert).handle().status());
                        break;
                    case BLOCK_FINISHED:
                        log("HERE: " + ((BlockFinishedAlert) alert).handle().status().progress());
                        progress = (int) (((BlockFinishedAlert) alert).handle().status().progress() * 100);
                        if (grade < progress / 20) {
                            int index = (int) (((BlockFinishedAlert) alert).pieceIndex());
                            log("index: " + index);
                            grade += 1;
                            s.downloadRate();
                            log(progress + " %  downloaded");
                        }
                        log("BLOCK_FINISHED");
                        break;
                    case STATE_UPDATE:
                        log(((StateUpdateAlert) alert).message());
                        break;
                    case METADATA_RECEIVED:
                        log("metadata received");
                        break;
                    case DHT_ERROR:
                        log("dht error");
                        log(((DhtErrorAlert) alert).message());
                        break;
                    default:
                        break;
                }
            }
        };
        s.addListener(l);
        if (s.isRunning() != true)
            s.start();
        if (link.startsWith("magnet:?")) {
            boolean bool = waitForNodesInDHT(s);
            if(!bool){
                torrentName = "DHT bootstrap timeout Please retry download";
                isTimedOut = true;
                isPaused = true;
                return;
            }else{
                byte[] data = s.fetchMagnet(link, 30);
                if(data == null){
                    torrentName = "Download timeout please retry";
                    isTimedOut = true;
                    isPaused = true;
                    return;
                }
                TorrentInfo ti = TorrentInfo.bdecode(data);
                torrentInfo = ti;
                torrentName = ti.name();
                log(Entry.bdecode(data).toString());
                log("is valid ? =" + ti.isValid());
                s.download(ti, saveDir);
                log("torrent added with name = " + ti.name());
                log(s.find(ti.infoHash()).isValid() + " isvalid");
                torrentHandle = s.find(ti.infoHash());
                log("torrent added to session");
            }
        }
    }


    public int getProgress(){
        if(torrentInfo !=null){
            return (int) (s.find(torrentInfo.infoHash()).status().progress() * 100);
        }else{
            return 0;
        }
    }

    public String getTotalDownload(){
        if(torrentInfo !=null) {
            float totalDone = s.find(torrentInfo.infoHash()).status().totalDone()/1000;
            String sTotalDone = "";
            if(totalDone >= 1000000){
                totalDone /= 1000000;
                sTotalDone = (Math.round(totalDone * 100.0) / 100.0) + " GB";
            }else if(totalDone >= 1000){
                totalDone /= 1000;
                sTotalDone = (Math.round(totalDone * 100.0) / 100.0) + " MB";
            }else{
                sTotalDone = totalDone + " KB";
            }

            float totalSize = s.find(torrentInfo.infoHash()).status().total()/1000;
            String sTotalSize = "";
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
                return String.valueOf(s.find(torrentInfo.infoHash()).status().state());
            }
        }else{
            return "";
        }
    }

    //get speed based on torrent state
    //seeding = upload speed
    //download = download speed
    public String getSpeed(){
        if(torrentInfo !=null) {
            if(isPaused){
                return "--";
            }else {
                if(torrentHandle.status().isSeeding()){
                    float uploadSpeed = s.find(torrentInfo.infoHash()).status().uploadRate()/1000;
                    String sUploadSpeed = "";
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
                    float downloadSpeed = s.find(torrentInfo.infoHash()).status().downloadRate()/1000;
                    String sDownloadSpeed = "";
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
        isSelected = true;
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public void removeTorrent(){
        s = null;
    }
}