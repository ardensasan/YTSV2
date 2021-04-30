package com.example.yts;

import android.content.Context;
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
        holder.tv_download_speed.setText(torrent.getDownloadSpeed());
        holder.pb_download_progress.setProgress(torrent.getProgress());
        holder.tv_download_state.setText(torrent.getTorrentState());
        holder.tv_total_download_done.setText(torrent.getTotalDone());
        holder.tv_total_download_size.setText(torrent.getTotalSize());
        holder.tv_total_download_percentage.setText(String.valueOf(torrent.getProgress()));
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
        TextView tv_total_download_done;
        TextView tv_total_download_size;
        TextView tv_total_download_percentage;
        ImageButton ibtn_download_action;
        public DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_download_name = itemView.findViewById(R.id.tv_download_name);
            tv_download_speed = itemView.findViewById(R.id.tv_download_speed);
            pb_download_progress = itemView.findViewById(R.id.pb_download_progress);
            tv_download_state = itemView.findViewById(R.id.tv_download_state);
            tv_total_download_done = itemView.findViewById(R.id.tv_total_download_done);
            tv_total_download_size = itemView.findViewById(R.id.tv_total_download_size);
            tv_total_download_percentage = itemView.findViewById(R.id.tv_total_download_percentage);
            ibtn_download_action = itemView.findViewById(R.id.ibtn_download_action);
        }
    }

}


