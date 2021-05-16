package com.example.yts.ui.downloads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yts.R;
import com.example.yts.torrent.Torrent;
import com.example.yts.torrent.TorrentDownloadsList;

import java.util.ArrayList;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>{
    private ArrayList<Torrent>  torrentDownloadsList;
    private Activity activity;
    public DownloadAdapter(ArrayList<Torrent> torrentDownloadsList, Activity activity){
        this.torrentDownloadsList = torrentDownloadsList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_downloads,parent,false);
        return new DownloadViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        Torrent torrent = torrentDownloadsList.get(position);
        holder.tv_download_name.setText(torrent.getTorrentName());
        holder.pb_download_progress.setProgress(torrent.getProgress());
        holder.tv_download_state.setText(torrent.getTorrentState());
        holder.tv_total_download.setText(torrent.getTotalDownload());
        holder.tv_total_download_percentage.setText(torrent.getProgress() + "%");
        holder.tv_download_speed.setText(torrent.getSpeed());

        holder.llh_downloads.setOnLongClickListener(v -> {
            torrent.setIsSelected(true);
            ((TorrentDownloadsList) activity.getApplication()).setIsHighlighted(true);
            return false;
        });

        if(torrent.getIsSelected()){
            holder.ibtn_download_action.setImageResource(android.R.color.transparent);
        }else if(torrent.getIsPaused()){
            holder.ibtn_download_action.setImageResource(R.drawable.ic_baseline_resume_circle_outline_24);
        }else{
            holder.ibtn_download_action.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        }

        final boolean[] deleteFolder = {false};
        holder.ibtn_download_action.setOnClickListener(v -> {
            if(torrent.getIsSelected()){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle("Delete "+torrent.getTorrentName()+"?");
                alertDialogBuilder.setCancelable(true);

                String[] strings = {"Also delete files in storage"};
                boolean[] checkedItems = {false};
                alertDialogBuilder.setMultiChoiceItems(strings, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        deleteFolder[0] = isChecked;
                    }
                });

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((TorrentDownloadsList) activity.getApplication()).removeTorrent(torrent,deleteFolder[0]);
                            }
                        });
                alertDialogBuilder.setNegativeButton("No", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else if(torrent.getIsPaused()){
                torrent.resumeTorrent();
            }else{
                torrent.pauseTorrent();
            }
        });
        if(torrent.getIsSelected()){
            holder.llh_downloads.setBackgroundColor(Color.CYAN);
        }else{
            holder.llh_downloads.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return torrentDownloadsList.size();
    }


    public class DownloadViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llh_downloads;
        TextView tv_download_name;
        TextView tv_download_speed;
        ProgressBar pb_download_progress;
        TextView tv_download_state;
        TextView tv_total_download;
        TextView tv_total_download_percentage;
        ImageButton ibtn_download_action;
        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            llh_downloads = itemView.findViewById(R.id.llh_downloads);
            tv_download_name = itemView.findViewById(R.id.tv_download_name);
            pb_download_progress = itemView.findViewById(R.id.pb_download_progress);
            tv_download_state = itemView.findViewById(R.id.tv_download_state);
            tv_total_download = itemView.findViewById(R.id.tv_total_download);
            tv_total_download_percentage = itemView.findViewById(R.id.tv_total_download_percentage);
            tv_download_speed = itemView.findViewById(R.id.tv_download_speed);
            ibtn_download_action = itemView.findViewById(R.id.ibtn_download_action);
        }
    }
}


