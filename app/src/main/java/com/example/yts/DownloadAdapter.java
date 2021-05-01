package com.example.yts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yts.torrent.Torrent;

import java.util.ArrayList;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> implements View.OnClickListener{
    ArrayList<Torrent>  torrentDownloadsList;

    public DownloadAdapter(ArrayList<Torrent> torrentDownloadsList){
        this.torrentDownloadsList = torrentDownloadsList;
    }
    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_downloads,parent,false);
        DownloadViewHolder downloadViewHolder = new DownloadViewHolder(view);
        view.setOnClickListener(this::onClick);
        return downloadViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        Torrent torrent = torrentDownloadsList.get(position);
        holder.tv_download_name.setText(torrent.getTorrentName());
        holder.pb_download_progress.setProgress(torrent.getProgress());
        holder.tv_download_state.setText(torrent.getTorrentState());
        holder.tv_total_download.setText(torrent.getTotalDownload());
        holder.tv_total_download_percentage.setText(String.valueOf(torrent.getProgress()) + "%");
        holder.tv_download_speed.setText(torrent.getDownloadSpeed());
        if(torrent.getIsPaused()){
            holder.ibtn_download_action.setImageResource(R.drawable.ic_baseline_resume_circle_outline_24);
        }else{
            holder.ibtn_download_action.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
        }
        holder.ibtn_download_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(torrent.getIsPaused()){
                    torrent.resumeTorrent();
                }else{
                    torrent.pauseTorrent();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return torrentDownloadsList.size();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"yawa",Toast.LENGTH_SHORT).show();
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder{
        TextView tv_download_name;
        TextView tv_download_speed;
        ProgressBar pb_download_progress;
        TextView tv_download_state;
        TextView tv_total_download;
        TextView tv_total_download_percentage;
        ImageButton ibtn_download_action;
        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
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


