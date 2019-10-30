package com.example.drowsinessdetector;

import android.os.AsyncTask;
import android.util.Log;
import android.hardware.Camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoSender extends AsyncTask<String, Void, Void> {

    File vid;

    // constructor ; store video to send to AWS
    public VideoSender(File v) {
        this.vid = v;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d("VideoSender", "doInBackground()");
        try{
            URL url = new URL(params[0]);
            Log.d("VideoSender", "URL received: " + url.toString());

            HttpURLConnection urlConnection  = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");

            Log.d("VideoSender", "length of video=" + this.vid.length());
            byte[] b = new byte[(int) vid.length()];
            FileInputStream fileInputStream = new FileInputStream(vid);
            fileInputStream.read(b);
            OutputStream out = urlConnection.getOutputStream();
            out = new BufferedOutputStream(out);
            out.write(b);

            urlConnection.disconnect();
        } catch(Exception e) {
            Log.d("VideoSender", "Failed to send video");
        }

        return null;
    }

}
