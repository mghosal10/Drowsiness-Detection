# Drowsiness Detector Android App

## How to Run
1. Clone this repository `git clone https://github.com/gwint/Drowsiness-Detection.git`
2. Download and open [Android Studio](https://developer.android.com/studio/install)
3. Click `Open an Existing Android Studio Project` from Android Studio, and load the directory at the path: `Drowsiness-Detection/Android/DrowsinessDetector`
4. Click the top-right triangle button `Run App` to launch the app.

## Files
Custom Java classes are located in: `DrowsinessDetector/app/src/main/java/com/example/drowsinessdetector/`:
* `MainActivity` The first screen when the app is first launched. Should replace this screen with a proper Log In page eventually.
* `HomeActivity` The main menu of the app once the user logs in. From here, the user can start the detector camera, or check their streak.
* `CameraActivity` Detector camera. Video recorded through this screen is sent to the AWS server for analysis.
* `CameraPreview` Helper class of `CameraActivity`. Sets up the Camera object and displays the camera preview in the `CameraActivity`'s layout.

## Resources
* Android
    * [Developer Fundamentals](https://codelabs.developers.google.com/android-training/)
    * [Documentation](https://developer.android.com/docs/)
    * [Camera API](https://developer.android.com/guide/topics/media/camera#custom-camera)
