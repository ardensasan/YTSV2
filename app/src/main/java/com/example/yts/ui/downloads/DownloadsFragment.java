package com.example.yts.ui.downloads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yts.R;

public class DownloadsFragment extends Fragment {

    private DownloadsViewModel downloadsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        downloadsViewModel =
                new ViewModelProvider(this).get(DownloadsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_downloads, container, false);
        downloadsViewModel.startTimer();
        final TextView textView = root.findViewById(R.id.text_notifications);
        Button button = root.findViewById(R.id.button);
        downloadsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}