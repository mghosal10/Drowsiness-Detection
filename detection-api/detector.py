import dlib
import cv2
import numpy
from imutils import face_utils
from scipy.spatial import distance as dist
import dotenv
import os

"""
A class to represent a machine capable of determing whether or not a
person appears drowsy based on an ordered collection of images provided in
short bursts.

Attributes
----------
_consecutiveDrowsyFrames: An integer representing the number of consecutive
    frames in which the subject has thier eyes closed.

Methods
-------
detect(images)
    Returns True if, after analyzing images, it is determined that the person
    depicted in the images in drowsy, False otherwise.
"""
class DrowsinessDetector:
    def __init__(self):
        dotenv.load_dotenv()
        self._consecutiveDrowsyFrames = 0
        self._maxDrowsyFramesBeforeSignal = os.getenv("FRAMES_BEFORE_DROWSINESS_CONFIRMED")

    """
    Analyzes eyes appearing in images and makes a determination based on
    the number of frames for which the face in the images has had his/her
    eyes closed as to whether the images depict a drowsy person and makes
    a determination one way or the other.

    @param images List[List[List[int]]] A 3-dimensional array representing a
        a collection of images including only a single face.
    @return bool True if the enough frames have passed that the person depicted
        in the images can be considered drowsy, else False.
    """
    def detect(self, images):
        ## Get facial landmarks (namely, left and right eyes)
        landmarkDetector = dlib.get_frontal_face_detector()
        predictor = dlib.shape_predictor("/home/gregory/Downloads/shape_predictor_68_face_landmarks.dat")
        img = dlib.load_rgb_image("testImage.png")
        dets = landmarkDetector(img, 1)
        print("num faces: ", len(dets))

        # Find the 5 face landmarks we need to do the alignment.
        faces = dlib.full_object_detections()
        faces.append(predictor(img, dets[0]))

        # grab the indexes of the facial landmarks for the left and
        # right eye, respectively
        (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
        (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]

        print(faces)
        shape = face_utils.shape_to_np(faces[0])
        print(shape)

        leftEye = shape[lStart:lEnd]
        rightEye = shape[rStart:rEnd]
        leftEAR = self.eye_aspect_ratio(leftEye)
        rightEAR = self.eye_aspect_ratio(rightEye)

        print(leftEye)

        leftEyeHull = cv2.convexHull(leftEye)
        rightEyeHull = cv2.convexHull(rightEye)
        cv2.drawContours(img, [leftEyeHull], -1, (0, 255, 0), 1)
        cv2.drawContours(img, [rightEyeHull], -1, (0, 255, 0), 1)

##        cv2.imshow("sklfjd", img)
##        cv2.waitKey()

        ear = (leftEAR + rightEAR) / 2
        print(ear)

        return True


    """
    Calculates the eye-aspect ratio described in
    http://vision.fe.uni-lj.si/cvww2016/proceedings/papers/05.pdf.  Eye-
    aspect ratio is the ratio of height to width of the eyes.

    @param eye ndarry An array describing the locations of landmarks within
        an eye detected in an image.
    @return double The ration between the height and width of an eye.
    """
    def eye_aspect_ratio(self, eye):
        # compute the euclidean distances between the two sets of
        # vertical eye landmarks (x, y)-coordinates
        A = dist.euclidean(eye[1], eye[5])
        B = dist.euclidean(eye[2], eye[4])

        # compute the euclidean distance between the horizontal
        # eye landmark (x, y)-coordinates
        C = dist.euclidean(eye[0], eye[3])

        ear = (A + B) / (2.0 * C)

        return ear

if __name__ == "__main__":
    d = DrowsinessDetector()
    d.detect([])
