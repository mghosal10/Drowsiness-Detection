package com.example.drowsinessdetector;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import android.hardware.Camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoSender extends AsyncTask<String, Void, Void> {

    //File vid;
    URL serverUrl;
    MediaRecorder mrec; // for stopping the recorder before sending
    String outfile_path; // where the video is saved on the Android phone
    int interval = 10; // time between sending video ; in miliseconds

    HttpURLConnection urlConnection;

//    public VideoSender(File v) {
//        this.vid = v;
//    }

    // constructor ; store video to send to AWS
    public VideoSender(MediaRecorder rec, String path) {
        this.mrec = rec;
        this.outfile_path = path;
    }

    // when task is cancelled (triggered by pressing Stop button)
    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("VideoSender", "Terminating VideoSender and closing HTTP connection");
        urlConnection.disconnect();
    }

    // sends a video periodically
    // https://stackoverflow.com/questions/5017093/upload-video-from-android-to-server
    @Override
    protected Void doInBackground(String... params) {

        String boundary = "*****";
        int serverResponseCode;

        try {
            this.serverUrl = new URL(params[0]);
            // create a connection with server
            Log.d("VideoSender", "Server URL: " + serverUrl.toString());
            urlConnection = (HttpURLConnection) serverUrl.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestMethod("POST");
            //add the content type of the request, most post data is of this type
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            urlConnection.setRequestProperty("uploaded_file", this.outfile_path);
        } catch(Exception e) {
            Log.d("VideoSender", "Failed to create HTTP connection.");
            return null;
        }

        int num_sent = 0; // only for debugging purposes
        OutputStream out;

        while(!isCancelled()) { // repeat until Stop button is pressed
            Log.d("VideoSender", "doInBackground()");
            try {
                Log.d("VideoSender", "Sending video #" + Integer.toString(num_sent));
                // stop recorder
                //mrec.stop();

                File vid = new File(this.outfile_path);
                if (!vid.isFile()) {
                    Log.e("VideoSender", "Video does not exist");
                    break;
                }

                Log.d("VideoSender", "length of video=" + vid.length());

                byte[] b = new byte[(int) vid.length()];

                //add the content length of the post data
                urlConnection.addRequestProperty("Content-Length", Integer.toString((int) vid.length()));

                // urlConnection.connect();

                FileInputStream fileInputStream = new FileInputStream(vid);
                fileInputStream.read(b);
                out = urlConnection.getOutputStream();
                out = new BufferedOutputStream(out);
                out.write(b); // write to server
                out.flush();
                // out.close();

                // Response from the server (code and message)
                serverResponseCode = urlConnection.getResponseCode();
                String serverResponseMessage = urlConnection.getResponseMessage();
                Log.d("VideoSender", "Server response: " + serverResponseMessage +
                        "; code=" + Integer.toString(serverResponseCode));

                num_sent++; // number of videos sent

                // start the recorder again
                //mrec.start();
                Thread.sleep(interval);

                // urlConnection.disconnect(); should do this when the thread exits
            } catch (Exception e) {
                Log.d("VideoSender", "Failed to send video");
                break;
            }
        } // while(1) ; end

        return null;
    }

    // This old version only sends one video at a time
//    @Override
//    protected Void doInBackground(String... params) {
//        Log.d("VideoSender", "doInBackground()");
//        try{
//            URL url = new URL(params[0]);
//            Log.d("VideoSender", "URL received: " + url.toString());
//
//            HttpURLConnection urlConnection  = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            urlConnection.setChunkedStreamingMode(0);
//            urlConnection.setRequestMethod("POST");
//
//            Log.d("VideoSender", "length of video=" + this.vid.length());
//            byte[] b = new byte[(int) vid.length()];
//            FileInputStream fileInputStream = new FileInputStream(vid);
//            fileInputStream.read(b);
//            OutputStream out = urlConnection.getOutputStream();
//            out = new BufferedOutputStream(out);
//            out.write(b);
//
//            urlConnection.disconnect();
//        } catch(Exception e) {
//            Log.d("VideoSender", "Failed to send video");
//        }
//
//        return null;
//    }

}
