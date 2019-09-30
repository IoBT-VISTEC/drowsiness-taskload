#!/usr/bin/env python
# coding: utf-8

import threading, Tkinter, cv2, time, PIL.Image, PIL.ImageTk, json
from roslibpy import Ros, Topic, Message
from playsound import playsound
import numpy as np
import time, os, fnmatch, shutil
import datetime
import paho.mqtt.client as mqtt
import json

class MyOVBox(OVBox):
	def __init__(self):
		OVBox.__init__(self)
		self.signalHeader = None

		self.ros_client = Ros(host='10.204.231.147', port=9090)  
		#self.listener = Topic(self.ros_client, '/chatter', 'std_msgs/String')
		self.publisher = Topic(self.ros_client, '/chatter', 'std_msgs/String')
		self.ros_client.on_ready(self.subscribing, run_in_thread=True)
		self.phase = 0
		self.parameters = {"count": "4", "scenario": "4", "gender": "Male", "age": "25", "subjectno": "55", "phase": 5, "type": 2}
		# ros_client.on_ready(start_receiving, run_in_thread=True)		

	def initialize(self):
		self.counter = 0
		t = threading.Thread(target=self.worker)
		t.start()
		s = threading.Thread(target =self.workerMQTT)
		s.start()
		# self.ros_client.run_forever()
		return

	def on_connect(self, client, userdata, flags, rc):
		print("Connected with result code "+str(rc))

		# Subscribing in on_connect() means that if we lose the connection and
		# reconnect then subscriptions will be renewed.
		self.client.subscribe("topic/parameters")

	# The callback for when a PUBLISH message is received from the server.
	def on_message(self, client, userdata, msg):
		#print(msg.topic+" "+str(msg.payload))
		parameters = msg.payload
		print("parameters = ", parameters)	
		self.parameters = json.loads(parameters)
		self.phase = int(self.parameters.get('phase'))
		print("phase = ", self.phase)

	def worker(self):
		self.ros_client.run_forever()		

	def workerMQTT(self):	
		self.client = mqtt.Client()
		self.client.on_connect = self.on_connect
		self.client.on_message = self.on_message

		self.client.connect("localhost", 1883, 60)

		# Blocking call that processes network traffic, dispatches callbacks and
		# handles reconnecting.
		# Other loop*() functions are available that give a threaded interface and a
		# manual interface.
		self.client.loop_forever()

	def process(self):
		self.counter += 1
		for chunkIndex in range( len(self.input[0]) ):
			if(type(self.input[0][chunkIndex]) == OVSignalHeader):
				self.signalHeader = self.input[0].pop()				
				outputHeader = OVSignalHeader(
				self.signalHeader.startTime, 
				self.signalHeader.endTime, 
				[1, self.signalHeader.dimensionSizes[1]], 
				['Mean']+self.signalHeader.dimensionSizes[1]*[''],
				self.signalHeader.samplingRate)
				
				self.output[0].append(outputHeader)
			
			elif(type(self.input[0][chunkIndex]) == OVSignalBuffer):
			#Check if Condition != 0 , Meaning user has been clicked start scenario				
			#if(self.phase != 0):								
				chunk = self.input[0].pop()
				numpyBuffer = np.array(chunk).reshape(tuple(self.signalHeader.dimensionSizes))
				#with open("C:/Users/h4wk/Desktop/Temp/foo.csv",'ab') as f:
				#path = os.getcwd() + datetime.datetime.now().strftime("%B %d, %Y") + ".csv"
				# if(self.phase != 0):				
				path = self.setting['OutputPath'] + '/BCI_GTEC_' + datetime.datetime.now().strftime("%Y%m%d_" + self.setting['subject_no']) + ".csv"
				with open(path ,'ab')	as f:		
					#t = time.localtime()
					#print("Current path:" + path)
					x = datetime.datetime.now()
					#a = np.asarray([[time.mktime(x.timetuple()), 
					#Header Pattern: Timestamp, phase, scenario, subjectno, gender, age, type, count
					a = np.asarray(int(round(time.time() * 1000)), dtype=object)
					# print(a)										
					# np.savetxt(f, a, delimiter=",", fmt='%5s')
					for channel in numpyBuffer:
						channel = np.insert(channel, 0, a, axis=0)
						# print(channel.shape)	
						np.savetxt(f, [channel], delimiter=",")
				#==================Sending to ROS Server==========================#
				#print(self.counter)
				# if(self.counter >= 20):
				# 	self.counter = 0
				# 	#self.ros_client.on_ready(self.publisher.publish(Message({'data': numpyBuffer})))
				# 	isConnected = self.ros_client.is_connected
				# 	#print("isConnected = ", isConnected)
				# 	if(isConnected):
				# 		print("sending data... ", numpyBuffer.tolist())	
				# 		#self.publisher.publish(Message({'data': numpyBuffer.tolist()}))
				# 		a = " ".join(str(i) for i in numpyBuffer.tolist())
				# 		self.publisher.publish(Message({'data': a}))
				# 		#self.publisher.publish(Message({'data': 'hello test'}))
				# 		#self.publisher.unadvertise()
				# ==================End Sending to ROS Server==========================#						
				numpyBuffer = numpyBuffer.mean(axis=0)				
				chunk = OVSignalBuffer(chunk.startTime, chunk.endTime, numpyBuffer.tolist())
				self.output[0].append(chunk)
				
			elif(type(self.input[0][chunkIndex]) == OVSignalEnd):
				self.output[0].append(self.input[0].pop())

	def subscribing(self):
		self.publisher.subscribe(receive_message)

	def uninitialize(self):
		# nop
		return

def receive_message(message):
	# context['counter'] += 1
	print(message['data'])
	#assert message['data'] == 'hello world', 'Unexpected message content'

def add_time_features(data):
	results = []
	if len(data.shape) == 3:
		for i, x in enumerate(data):
			for j, xx in enumerate(x):
				r = xx.tolist() + [j]
				results.append(np.array(r))
	else:
		for j, xx in enumerate(data):
			r = xx.tolist() + [j%48]
			results.append(np.array(r))
	return np.array(results) 

box = MyOVBox()
