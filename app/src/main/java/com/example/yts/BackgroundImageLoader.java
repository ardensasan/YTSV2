package com.example.yts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundImageLoader {
    private String imageURL;
    private ImageButton imageButton;
    public BackgroundImageLoader(ImageButton imageButton, String imageURL) {
        this.imageButton = imageButton;
        this.imageURL = imageURL;
    }

    public void loadImage(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    URL url = new URL(imageURL);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    imageButton.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
