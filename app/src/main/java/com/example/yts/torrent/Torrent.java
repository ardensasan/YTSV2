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

    public Torrent(String magnetURI){
        this.magnetURI = magnetURI;
    }

    public String getMagnetURI(){
        return magnetURI;
    }

    public void downloadStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                s = new SessionManager();
                try {
                    startdl(magnetURI, s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void waitForNodesInDHT(SessionManager s) throws InterruptedException {
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
            log("DHT bootstrap timeout retrying");
            waitForNodesInDHT(s);
        }
        return;
    }

    public int getProgress(){
        if(torrentInfo !=null){
            return (int) (s.find(torrentInfo.infoHash()).status().progress() * 100);
        }else{
            return 0;
        }
    }

    public String getTotalSize(){
        if(torrentInfo !=null) {
            return String.valueOf((float)s.find(torrentInfo.infoHash()).status().total()/1000);
        }else {
            return "";
        }
    }

    public String getTotalDone(){
        if(torrentInfo !=null) {
            return String.valueOf((float)s.find(torrentInfo.infoHash()).status().totalDone()/1000);
        }else{
            return "";
        }
    }

    public String getTorrentState(){
        if(torrentInfo !=null) {
            return String.valueOf(s.find(torrentInfo.infoHash()).status().state());
        }else{
            return "";
        }
    }

    public String getDownloadSpeed(){
        if(torrentInfo !=null) {
            return String.valueOf((float)s.find(torrentInfo.infoHash()).status().downloadRate()/1000);
        }else{
            return "";
        }
    }
    public String getTorrentName(){
        if(torrentInfo !=null) {
            return torrentInfo.name();
        }else{
            return "Getting File Name";
        }
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
                        //((AddTorrentAlert) alert).handle().setFlags(TorrentFlags.SEQUENTIAL_DOWNLOAD);
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
            waitForNodesInDHT(s);
            byte[] data = s.fetchMagnet(link, 30);
            TorrentInfo ti = TorrentInfo.bdecode(data);
            torrentInfo = ti;
            log(Entry.bdecode(data).toString());
            log("is valid ? =" + ti.isValid());
            s.download(ti, saveDir);
            log("torrent added with name = " + ti.name());
            //storrent.addTh(s.find(ti.infoHash()), name);
            log(s.find(ti.infoHash()).isValid() + " isvalid");
            log("torrent added to session");
            //this.videoname = ti.name();
            int i = 0;
            while (i < 10){
                TimeUnit.SECONDS.sleep(1);
                log(s.find(ti.infoHash()).status().state() + " state");
                log(s.find(ti.infoHash()).status().progress() * 100 + " progress");
            }
        }
    }
}