# EE 542 Fall 2019 Final Project
# House Watchdog, got 6
# Auther: Xin Huang

# Pre:
# 1. pip install boto3
# 2. pip install awscli
# 3. aws console -> My Security Credentials -> Access Keys
# 4. terminal: aws configure

import boto3

def uploadToS3(filename):
	client = boto3.client('s3', region_name='us-west-2')
	# upload_file(filename, bucketname, objectname = None)
	# objectname is same as filename by default
	response = client.upload_file(filename, 'usc-ee542-xin-fall2019', 'lena.jpeg')
	if (response == False):
		print("Fail to upload file.\n")

filename = '/Users/huangxin/Downloads/lena.jpeg'
uploadToS3(filename)