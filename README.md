
Hardware Specifications:
Sensors:
Camera: To capture heart rate through the user placing a finger over the camera and flash, which will allow the app to detect heart rate based on color variations in the video.
Flashlight: Used to brighten the user's finger for better detection of blood flow during heart rate calculation.
Accelerometer Sensor: It will measure the user's chest movement when they lie down with the phone on their chest for 45 seconds to calculate respiratory rate 
Software Specification: 
Operating System: Android 14+ (API level 34 or above) for compatibility with new features, as Health_Dev supports Android-based development.
Database: Room DB for local storage of heart rate, respiratory rate, and symptom data.
Programming Language: Kotlin
User Interface (UI): A simple interface where the user can get heart rate and respiratory rate by pressing the respective buttons and track and provide ratings for different symptoms.
Algorithm Specifications:
Heart rate Measurement:
Input: The user places a finger over the camera and flash and the video will be captured for 45 seconds.
Algorithm process: Analyze the variation in red color intensity across frames to calculate heart rate (Photoplethysmography)
Output: Heart rate will be calculated in beats per minute.
Respiratory Rate Measurement:
Input: The user lies down and places the phone on their chest and accelerometer, or orientation data will be collected.
Algorithm Process: Use peak detection in the chest movement to count breadths in beats per minute
Output: Respiratory rate in breaths per minute will be calculated.
Symptom Collection: 
Input: A list of 10 symptoms with severity ratings (1-5 stars) where 1 means lowest severity and 5 means highest severity.
Output: Symptom data collected and stored along with heart rate and respiratory rate in a local database.

