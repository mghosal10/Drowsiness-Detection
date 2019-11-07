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
from http.server import HTTPServer, BaseHTTPRequestHandler

from detector import DrowsinessDetector

class TestDetectionServer(BaseHTTPRequestHandler):
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
        #ctype, pdict = cgi.parse_header(self.headers['content-type'])
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
	#run(addr = "localhost", port = 8000)
	run(addr = "172.20.10.3", port=8000)
