# Detection Server

## Dependencies
1) Python3
2) opencv
3) imutils
4) Scipy
5) numpy
6) dotenv
7) dlib

## Using test detection server
Make sure you have installed all the dependencies mentioned above. 

## Setting the server up locally
1) Open up detectionserver.py and locate the program's main method.
2) By default, the server is listening on port 8000.
3) Start up the server by running "sudo python3 detectionserver.py" and wait for a message
indicating that the server has been started successfully to appear on stdout.

## Setting up the server via Amazon AWS EC2
1) Head over [here](https://signin.aws.amazon.com/signin?redirect_uri=https%3A%2F%2Fportal.aws.amazon.com%2Fbilling%2Fsignup%2Fresume&client_id=signup) and make an Amazon Web Services account.
2) We want to create an Amazon EC2 instance with enough storage for all neccesary dependencies.  A small ubuntu instance (t2.small with 2 GB of storage) is large enough.
3) Start up the instance via the AWS EC2 console and then use ssh to clone this repo onto the virtual machine.
4) Start up the detection server by running "sudo python3 detectionserver.py".
5) (Note about the android application) Use the url for your ec2 instance to update the android application so that video is sent to the server running on your ec2 instance.
