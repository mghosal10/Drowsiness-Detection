# Drowsiness Detector Android App

## How to Run
1. Clone this repository `git clone https://github.com/gwint/Drowsiness-Detection.git`
2. Download and open [Android Studio](https://developer.android.com/studio/install)
3. Click `Open an Existing Android Studio Project` from Android Studio, and load the directory at the path: `Drowsiness-Detection/Android/DrowsinessDetector`
4. Click the top-right triangle button `Run App` to launch the app.

## Running with Test Server
1. Create a WiFi hotspot with personal phone. Then connect laptop (where server script will run) and Android phone to the WiFi hotspot.
2. Obtain the new IP address of the laptop and correctly set the IP address in `CameraActivity.java` at `serverUrl`. Also change the IP address in the server script `detectionserver.py`.
3. Start the server by running `python3 /detection-api/detectionserver.py` server.
4. Start the app from Android Studio.

## Java Classes
Custom Java classes are located in: `DrowsinessDetector/app/src/main/java/com/example/drowsinessdetector/`:
* `MainActivity` The login page when the app is first launched.
* `HomeActivity` The main menu of the app once the user logs in. From here, the user can start the detector camera, or check their streak.
* `CameraActivity` Detector camera. Video recorded through this screen is sent to the AWS server for analysis.
* `CameraPreview` Helper class of `CameraActivity`. Sets up the Camera object and displays the camera preview in the `CameraActivity`'s layout.
* `VideoSender` Background thread that sends the video to AWS server

## Resources
* Android
    * [Developer Fundamentals](https://codelabs.developers.google.com/android-training/)
    * [Documentation](https://developer.android.com/docs/)
    * [Camera API](https://developer.android.com/guide/topics/media/camera#custom-camera)
	* [HttpURLConnection](https://developer.android.com/reference/java/net/HttpURLConnection.html)
	* [AsyncTask](https://developer.android.com/reference/android/os/AsyncTask)
