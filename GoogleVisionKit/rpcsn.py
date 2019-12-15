#!/usr/bin/env python3
from xmlrpc.server import SimpleXMLRPCServer
from xmlrpc.server import SimpleXMLRPCRequestHandler
import json
import urllib.request
import logging
import boto3
import os
import time
from botocore.exceptions import ClientError
from gpiozero import Servo

bucket_name = 'usc-ee542-xin-fall2019'
bucket_path = ''

api_url= 'http://54.215.225.213:8080/HouseWatchDog/post'
headers = {'Content-Type': 'application/json'}
file_url_base = "https://usc-ee542-xin-fall2019.s3-us-west-1.amazonaws.com/"
#photo_dir = '/home/pi/Pictures/'
photo_dir = '/home/pi/AIY-projects-python/src/'

#Initialize
#servo = Servo(23)
#time.sleep(1)
#servo.detach()
#print("Pin initialized")
ss = 0

def capture(x):
    global ss
    ss = x
    if ss == 0:
        msg = "Camera deactivated!"
    else:
        msg = "Camera activated!"
    return msg

def stop():
    global ss
    ss = 0
    msg = "Camera deactivated!"
    return msg

def captureo():
    global ss
#    if ss == 0:
#        servo.max()
#        ss == 1
#    else:
#        servo.min()
#        ss == 0
    ss = 1
#    time.sleep(5)
#    ss = 0
    msg = "Camera activated!"
    return msg
#    servo.detach()

def newest():
    files = os.listdir(photo_dir)
    paths = [os.path.join(photo_dir, basename) for basename in files]
    return max(paths, key=os.path.getctime)

# file
def post(filename, label):
    ctime = time.strftime('%Y-%m-%d %H:%M:%S')
    #ctime = "2019-11-30 23:47:30"
    payload = {}
    #payload['filename'] = 'testImage_jyn_003.jpeg'
    #payload['filename'] = ctime + '.jpeg'
    payload['filename'] = filename
    payload['camera_id'] = '0001'
    payload['capture_time'] = ctime
    payload['url'] = "https://usc-ee542-xin-fall2019.s3-us-west-1.amazonaws.com/" + filename
    payload['mlResult'] = label
    payload['comment'] = 'test post'
    json_data = json.dumps(payload).encode('utf-8')

    req = urllib.request.Request(api_url, json_data, headers)
    response = urllib.request.urlopen(req)
    if response.getcode() == 200:
        res = (response.read().decode('utf-8'))
        print(res)
        return json.loads(res)['Result']
    else:
        print("Connection failed!")
        return None

def upload_file(file_name, bucket, object_name=None):
    """Upload a file to an S3 bucket

    :param file_name: File to upload
    :param bucket: Bucket to upload to
    :param object_name: S3 object name. If not specified then same as file_name
    :return: True if file was uploaded, else False
    """

    # If S3 object_name was not specified, use file_name
    if object_name is None:
        object_name = file_name

    # Upload the file
    s3_client = boto3.client('s3', aws_access_key_id="AKIAIQY5H5EJXBMSLCEA", aws_secret_access_key="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")
    try:
        response = s3_client.upload_file(file_name, bucket, object_name, ExtraArgs={'ACL':'public-read'})
    except ClientError as e:
        logging.error(e)
        return False
    return True

def upload(file_name):
    """Exercise upload_file()"""

    # Set these values before running the program
    object_name = bucket_path + file_name
    file_name = photo_dir + file_name

    # Set up logging
    #logging.basicConfig(level=logging.DEBUG,
    #    format='%(levelname)s: %(asctime)s: %(message)s')

    # Upload a file
    response = upload_file(file_name, bucket_name, object_name)
    if response:
        #logging.info('File was uploaded')
        #return('File was uploaded')
        return(file_name)

# Restrict to a particular path.
class RequestHandler(SimpleXMLRPCRequestHandler):
    rpc_paths = ('/',)

# Create server
with SimpleXMLRPCServer(('0.0.0.0', 8000),
                        requestHandler=RequestHandler) as server:
    server.register_introspection_functions()

    # Register pow() function; this will use the value of
    # pow.__name__ as the name, which is just 'pow'.
    #server.register_function(pow)

    # Register a function under a different name
    #def adder_function(x, y):
    #    return x + y
    #server.register_function(adder_function, 'add')

    #def capture_function(x = None):
    #    trigger()
    #    return os.path.basename(newest())
    #server.register_function(capture_function, 'capture')

    def result_function(label, filename):
        #return x + y
        #print(label)
        #upload(filename)
        post(filename, label)
        msg = "Delivered"
        print(msg)
        return msg
    server.register_function(result_function, 'result')

    def camera_function():
        return ss
    server.register_function(camera_function, 'camera')

    def echo_function(x):
        return x
    server.register_function(echo_function, 'echo')

    # Register an instance; all the methods of the instance are
    #def post_function(x):
    #    return post_result(x)
    #server.register_function(post_function, 'post')
    server.register_function(post)

    server.register_function(capture)
    server.register_function(stop)
    server.register_function(upload)

    # published as XML-RPC methods (in this case, just 'mul').
    class MyFuncs:
        def mul(self, x, y):
            return x * y

    server.register_instance(MyFuncs())

    # Run the server's main loop
    server.serve_forever()

