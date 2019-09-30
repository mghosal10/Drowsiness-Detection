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
        self._consecutiveDrowsyFrames = 0

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
        return True
