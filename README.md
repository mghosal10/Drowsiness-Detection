# Drowsiness-Detection

## Dependencies
1) Python3
2) open-cv
3) imutils
4) Scipy
5) numpy
6) dotenv

## Using test detection server
1) Make sure you have all the dependancies mentioned earlier installed.
2) Start up the server by running "./detectionserver" and wait for a message
indicating that the server has been started successfully to appear on stdout.
3) Make an POST request to the server (which is running on localhost, port 8000).
  "curl -F "fileupload=@testImage.png" http://localhost:8000", where testImage.png
can be replaced by any image the user wants, will upload the image to the server
and return a json response indicating whether or not the subject in the image
has his/her eyes closed.
