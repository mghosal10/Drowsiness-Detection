#!/usr/bin/python3

"""
Very simple HTTP server in python (Updated for Python 3.7)
Usage:
    ./detectionserver.py
Send a GET request:
    curl http://localhost:8000
Send a HEAD request:
    curl -I http://localhost:8000
Send a POST request:
    curl -d "foo=bar&bin=baz" http://localhost:8000
"""
import cgi
import cv2
import numpy
import base64
import os
import dlib
from http.server import HTTPServer, BaseHTTPRequestHandler
from detector import DrowsinessDetector

class TestDetectionServer(BaseHTTPRequestHandler):
    detector = DrowsinessDetector()

    def __init__(self, request, client_address, server):
        print("Constructor called")
        BaseHTTPRequestHandler.__init__(self, request, client_address, server)

    def _set_headers(self):
        self.send_response(200)
        self.send_header("Content-type", "text/json")
        self.end_headers()

    def _html(self, message):
        """This just generates an HTML document that includes `message`
        in the body. Override, or re-write this do do more interesting stuff.
        """
        return message.encode("utf8")  # NOTE: must return a bytes object!

    def do_GET(self):
        self._set_headers()
        self.wfile.write(self._html("WELCOME TO OUR DROWSINESS DETECTOR"))

    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        print('Post!')

        response = {"drowsy": False}

        ## Save file temporarily
        tmpFile = open("tmp.mpg", 'wb')
        tmpFile.write(self.rfile.read(int(self.headers["content-length"])))
        tmpFile.close()

        ## Split video file into frames
        vidObj = cv2.VideoCapture("tmp.mpg")
        count = 0
        success = 1
        while success:
            success, image = vidObj.read()

            if count % 10 == 0:
                cv2.imwrite("tmpFrame.jpg", image)

                img = dlib.load_grayscale_image("tmpFrame.jpg")
                appearsDrowsy = TestDetectionServer.detector.areEyesClosed(img)
                if(TestDetectionServer.detector.isDrowsy()):
                    response["drowsy"] = True
                    break

                if os.path.exists("tmpFrame.jpg"):
                    os.remove("tmpFrame.jpg")

            count += 1
        print(count)

        ## Remove tmp file
        if os.path.exists("tmp.mpg"):
            os.remove("tmp.mpg")

        self._set_headers()
        self.wfile.write(self._html(str(response)))

        #ctype, pdict = cgi.parse_header(self.headers['content-type'])
        #print('ctype', ctype)
        #print('pdict', pdict)
        #pdict['boundary'] = bytes(pdict['boundary'], "utf-8")
        #postvars = cgi.parse_multipart(self.rfile, pdict)
        #imageArr = numpy.fromstring(postvars['fileupload'][0], numpy.uint8)
        #image = cv2.imdecode(imageArr, cv2.IMREAD_GRAYSCALE)
        #detector = DrowsinessDetector()
        #self._set_headers()
        #eyesClosed = detector.areEyesClosed(image)
        #responseMessage = "{drowsiness=%s}" % (str(eyesClosed))
        #self.wfile.write(self._html(responseMessage))


def run(server_class=HTTPServer, handler_class=TestDetectionServer, addr="localhost", port=8000):
    server_address = (addr, port)
    httpd = server_class(server_address, handler_class)

    print(f"Starting http server on {addr}:{port}")
    httpd.serve_forever()


if __name__ == "__main__":
	run(addr = "", port = 8000)
