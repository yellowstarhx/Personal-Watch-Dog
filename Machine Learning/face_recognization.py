"""Trigger PiCamera when face is detected."""
import argparse

import numpy as np
from aiy.leds import PrivacyLed, Leds, Pattern, Color
from aiy.toneplayer import TonePlayer
from aiy.vision.models import utils
from aiy.vision.inference import CameraInference, ModelDescriptor, ImageInference
from aiy.vision.models import face_detection
from PIL import Image, ImageDraw
import xmlrpc.client
import time
import logging
import boto3
from botocore.exceptions import ClientError

from picamera import PiCamera

input_img_width = 1640
input_img_height = 1232
output_img_size = 160

BUZZER_GPIO = 22
JOY_SOUND = ('C5q', 'E5q', 'C6q')
SAD_SOUND = ('C6q', 'E5q', 'C5q')
MODEL_LOAD_SOUND = ('C6w', 'c6w', 'C6w')
BEEP_SOUND = ('E6q', 'C6q')


bucket_name = 'usc-ee542-xin-fall2019'
bucket_path = ''

photo_dir = '/home/pi/AIY-projects-python/src/'

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

    # Upload a file
    response = upload_file(file_name, bucket_name, object_name)
    if response:
        #return('File was uploaded')
        return(file_name)

def classify_hand_gestures(img_inference,hands_images,model,labels,output_layer,threshold):
    for hand in hands_images:
        #print(type(img_inference))
        #print(hand.shape())
        result = img_inference.run(hand)
        print(result)
        model_output = process(result, labels, output_layer, threshold)
        return model_output

# Processes inference result and returns either
# (1) the index of most likely label with probability > threshold
# (2) index = len(labels) otherwise (i.e. none of hand gestures had a probablity > threshold
def process(result, labels, out_tensor_name, threshold):
    # MobileNet based classification model returns one result vector.
    assert len(result.tensors) == 1
    tensor = result.tensors[out_tensor_name]
    probs, shape = tensor.data, tensor.shape
    assert shape.depth == len(labels)
    pairs = [pair for pair in enumerate(probs) if pair[1] > threshold]
    pairs = sorted(pairs, key=lambda pair: pair[1], reverse=True)
    pair = pairs[0:1]
    if pair==[]:
        return len(labels)
    else:
        index, prob = pair[0]
        return index

# Read model labels
def read_labels(label_path):
    with open(label_path) as label_file:
        return [label.strip() for label in label_file.readlines()]

# Check if box boundaries are within the limits
def image_boundary_check(box):
    left, upper, right, lower = box
    #
    return (int(left)>0 and int(upper)>0 and
              int(right)< input_img_width and int(lower) < input_img_height and
              int(right)>int(left) and int(lower)>int(upper))

def crop_np(box, wpara, image_np):
    #x, y, w, h = box;
    left, upper, right, lower = box
    #left = x; right = x + w; lower = y; upper = y+h;
    box = (int(left), int(upper), int(right), int(lower))

        # cropped_image_np = image_np[int(upper):(int(lower) + 1), int(left):(int(right) + 1):, :]

    #if int(lower) > int(upper): temp = lower; lower = upper; upper = temp;
    #if int(right) < int(left):  temp = right; right = left; left = temp;
    #if (int(left) - int(wpara)) < 0 : left = wpara;
    #if (int(right) + int(wpara) + int(left)) > input_img_width: right = input_img_width-1-wpara-left;
    #if (int(lower + upper) > )
    if not image_boundary_check((left-wpara, upper, lower+upper, right+left+wpara)):
        return None
    cropped_image_np = image_np[int(upper):(int(lower) + int(upper)),
                       (int(left) - int(wpara)):(int(right) + int(left) + int(wpara)):, :]
    #left = int(left); right=int(right); lower=int(lower); upper=int(upper);
    #cropped_image_np = image_np[upper:lower, left:right+1, :]
    cropped_image = Image.fromarray(cropped_image_np, 'RGB')
    cropped_image = cropped_image.resize((output_img_size, output_img_size), Image.ANTIALIAS)
    return cropped_image
    # else:
    #     return None


def main():
    model_path = '/opt/aiy/models/retrained_graph.binaryproto'
    #model_path = '/opt/aiy/models/mobilenet_v1_160res_0.5_imagenet.binaryproto'
    label_path = '/opt/aiy/models/retrained_labels_new.txt'
    #label_path = '/opt/aiy/models/mobilenet_v1_160res_0.5_imagenet_labels.txt'
    model_path = '/opt/aiy/models/rg_v3_new.binaryproto'
    label_path = '/opt/aiy/models/retrained_labels_new.txt'
    input_height = 160
    input_width = 160
    input_layer = 'input'
    output_layer = 'final_result'
    threshold = 0.8
    # Model & labels
    model = ModelDescriptor(
        name='mobilenet_based_classifier',
        input_shape=(1, input_height, input_width, 3),
        input_normalizer=(128.0, 128.0),
        compute_graph=utils.load_compute_graph(model_path))
    labels = read_labels(label_path)
    new_labels = []
    for eachLabel in labels:
        if len(eachLabel)>1:
            new_labels.append(eachLabel)
    labels = new_labels
    #print(labels)
    s = xmlrpc.client.ServerProxy("http://aiy.mdzz.info:8000/")
    player = TonePlayer(BUZZER_GPIO, 10)
    player.play(*MODEL_LOAD_SOUND)
    while True:
        while True:
            if s.camera() == 1:
                print('vision kit is woken up')
                with Leds() as leds:
                    leds.pattern = Pattern.blink(100)
                    leds.update(Leds.rgb_pattern(Color.RED))
                    time.sleep(2.0)
                start_time = round(time.time())
                break
            time.sleep(0.2)
            print('no signal, sleeping...')

        with PiCamera() as camera:
            # Configure camera
            camera.sensor_mode = 4
            camera.resolution = (1664, 1232)  # Full Frame, 16:9 (Camera v2)
            camera.framerate = 30
            camera.start_preview()
            while True:
                # Do inference on VisionBonnet
                #print('Start capturing')
                with CameraInference(face_detection.model()) as inference:
                    for result in inference.run():
                        #print(type(result))
                        faces = face_detection.get_faces(result)
                        if len(faces) >= 1:
                            #print('camera captures...')
                            extension = '.jpg'
                            filename = time.strftime('%Y-%m-%d %H:%M:%S') + extension
                            camera.capture(filename)
                            image_npp = np.empty((1664 * 1232 * 3,), dtype=np.uint8)
                            camera.capture(image_npp, 'rgb')
                            image_npp = image_npp.reshape((1232, 1664, 3))
                            image_npp = image_npp[:1232, :1640, :]
                            # image = Image.open('jj.jpg')
                            # draw = ImageDraw.Draw(image)
                            faces_data = []
                            faces_cropped = []
                            for i, face in enumerate(faces):
                                # print('Face #%d: %s' % (i, face))
                                x, y, w, h = face.bounding_box
                                #print(x,y,w,h)
                                w_rm = int(0.3 * w / 2)
                                face_cropped = crop_np((x, y, w, h), w_rm, image_npp)
                                if face_cropped is None: continue #print('face_cropped None'); continue
                                # faces_data.append(image[y: y + h, x + w_rm: x + w - w_rm])
                                # image[y: y + h, x + w_rm: x + w - w_rm].save('1.jpg')
                                face_cropped.save('face_cropped_'+str(i)+'.jpg')
                                faces_cropped.append(face_cropped)
                                #break
                            break
                        # else:
                        #     tt = round(time.time()) - start_time
                        #     if tt > 10:
                        #         break
                    #print('face cutting finishes')

                #print(type(faces_cropped), len(faces_cropped))
                player.play(*BEEP_SOUND)
                flag = 0
                for eachFace in faces_cropped:
                    #print(type(eachFace))
                    if eachFace is None: flag = 1
                if (len(faces_cropped)) <= 0: flag = 1
                if flag == 1: continue
                with ImageInference(model) as img_inference:
                #with CameraInference(model) as img_inference:
                    print('Entering classify_hand_gestures()')
                    output = classify_hand_gestures(img_inference, faces_cropped, model=model, labels=labels,
                                                    output_layer=output_layer, threshold=threshold)
                #print(output)
                if (output == 3):
                    player.play(*JOY_SOUND)
                    print('Yani face detected')
                    print(s.result("Owner", filename))
                else:
                    player.play(*SAD_SOUND)
                    print('Suspicious face detected')
                    print(s.result("Unknown Face", filename))
                upload(filename)
                # Stop preview #
                #break
                while (s.camera()==0):
                    print('sleeping')
                    time.sleep(.2)
                print('Waken up')
            #camera.stop_preview()



if __name__ == '__main__':
    main()
