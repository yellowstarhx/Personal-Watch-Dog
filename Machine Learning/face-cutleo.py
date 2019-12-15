import cv2
import sys
import os
import glob



def cut_faces(image, faces_data):
    faces = []

    for (x, y, w, h) in faces_data:
        w_rm = int(0.3 * w / 2)
        faces.append(image[y: y + h, x + w_rm: x + w - w_rm])

    return faces

def resize(images, size=(160, 160)):
    images_norm = []
    for image in images:
        if image.shape < size:
            image_norm = cv2.resize(image, size,
                                    interpolation=cv2.INTER_AREA)
        else:
            image_norm = cv2.resize(image, size,
                                    interpolation=cv2.INTER_CUBIC)
        images_norm.append(image_norm)

    return images_norm

# for (x, y, w, h) in faces_data:#
#     cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)
path = 'dataset/*.jpg'
count = 0
for filename in glob.glob(path):
    print(filename)
    image = cv2.imread(filename)
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    faceCascade = cv2.CascadeClassifier(cv2.data.haarcascades + "haarcascade_frontalface_default.xml")
    faces_data = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.3,
        minNeighbors=3,
        minSize=(30, 30)
    )

    print("[INFO] Found {0} Faces.".format(len(faces_data)))
    faces_data = cut_faces(image, faces_data)
    faces_data = resize(faces_data)

    for face in faces_data:
        count += 1
        cv2.imwrite('yani-face/%s.jpeg' % (count), face)