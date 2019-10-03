package com.example.drowsinessdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class CameraActivity extends AppCompatActivity {

    private String outfile = "drowsy.3gp";
    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mrec;

    // get Camera instance
    public static Camera getCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch(Exception e) {
            Log.d("getCamera", "Camera does not exist.");
        }

        return c; // return Camera instance
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Log.d("CameraActiivty", "created CameraActivity");

        mCamera = getCamera();
        if(mCamera == null) return;

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    public void returnToMain(View view) {
        Log.d("CameraActivity", "Returning to Main.");
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
        finish(); // return to MainActivity
    }

    public void startCamera(View view) {
        if(mCamera == null) {
            Log.d("startCamera", "Failed to get camera.");
            return;
        }

        // at this point, Camera.open() was successfully called
        // https://developer.android.com/guide/topics/media/camera#custom-camera

        mrec = new MediaRecorder();
        if(mrec == null) {
            Log.d("startCamera", "Failed to get MediaRecorder.");
            return;
        }

        // unlock the camera
        mCamera.unlock();

        // configure MediaRecorder
        mrec.setCamera(mCamera);
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        // set output file
        String file_path = getExternalFilesDir(null).toString() + "/" + outfile;
        Log.d("startCamera", "file_path: " + file_path);
        mrec.setOutputFile(file_path);

        // set preview display
        mrec.setPreviewDisplay(mPreview.getSurfaceHolder().getSurface());

        // prepare media recorder before recording
        try {
            mrec.prepare();
        } catch(Exception e) {
            Log.d("startCamera", "Failed to prepare MediaRecorder");
            mrec = null;
        }

        Log.d("startCamera", "Camera is ready to record.");
        mrec.start();

    }

    public void stopCamera(View view) {
        if(mCamera != null && mrec != null) {

            // stop recorder
            mrec.stop();

            // reset configuration settings
            mrec.reset();

            // release media recorder
            mrec.release();

            // lock camera
            mCamera.lock();

            // stop the camera preview
            mCamera.stopPreview();

            // remove this MediaRecorder ?
            mrec = null;

            Log.d("stopCamera", "Releasing camera.");
            mCamera.release();
        } else {
            Log.d("stopCamera", "Attempted to stop with no MediaRecorder installed.");
        }
    }
}
