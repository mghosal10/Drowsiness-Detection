package com.example.drowsinessdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_VIDEO = 2;

    private String outfile = "drowsy.3gp";
    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mrec;

    private boolean isRecording = false;

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
        if(mCamera == null) {
            Log.d("CameraActivity", "Failed to get camera onCreate()");
            return;
        }

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

    private boolean startCamera() {
        if(mCamera == null) {
            Log.d("startCamera", "Failed to get camera.");
            return false;
        }

        // at this point, Camera.open() was successfully called
        // https://developer.android.com/guide/topics/media/camera#custom-camera

        mrec = new MediaRecorder();
        if(mrec == null) {
            Log.d("startCamera", "Failed to get MediaRecorder.");
            return false;
        }

        // unlock the camera
        mCamera.unlock();

        // configure MediaRecorder
        mrec.setCamera(mCamera);
        mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        // set output file

        // Use cache (which stores files temporarily) instead of external file
        String file_path = this.getCacheDir().toString() + "/" + outfile;
        //String file_path = getExternalFilesDir(null).toString() + "/" + outfile;
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
            return false;
        }

        Log.d("startCamera", "Camera is ready to record.");
        mrec.start();
        return true; // success
    }

    private void stopCamera() {
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

    private void setCameraButtonText(Button b, String s) {
        b.setText(s);
    }

    // Use one button for start, stop, record camera
    public void toggleCamera(View view) {
        Log.d("toggleCamera", "toggleCamera() was called.");
        // Create a listener for camera button
        final Button cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRecording) {
                            // stop recording and release camera
                            stopCamera();
                            setCameraButtonText(cameraButton, "Record");
                            isRecording = false;
                            Log.d("toggleCamera", "Stopped recording");
                        } else {
                            if(startCamera()) {
                                setCameraButtonText(cameraButton, "Stop");
                                isRecording = true;
                                Log.d("toggleCamera", "Started recording");
                            } else { // failed
                                stopCamera();
                                Log.d("toggleCamera", "Failed");
                            } // failed ; end
                        }
                    } // onClick() ; end
                } // new View.OnClickListener() ; end
        ); // cameraButton.setOnClickListener ; end
    }

    /* Saving media files */

    // create file for saving video
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "DrowsinessDetector");

        // create directory for this app, if it doesn't exist
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                Log.d("CameraActivity", "Failed to create a directory");
                return null;
            }
        }

        // create a media file
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            Log.d("CameraActivity", "Error. Only capturing videos");
            return null;
        }

        return mediaFile;
    }

    // create URI for image
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile((type)));
    }
}
