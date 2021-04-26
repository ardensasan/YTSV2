package com.example.yts.ui.downloads;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DownloadsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private int time = 0;

    public DownloadsViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void startTimer(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(time < 100){
                    try {
                        Thread.sleep(1000);
                        time++;
                        mText.postValue("yawa"+time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}