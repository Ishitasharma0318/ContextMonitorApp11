Question 1: Imagine you are new to the programming world and not proficient enough in coding. But you have a brilliant idea where you want to develop a context-sensing application like Project 1.  You come across the Heath-Dev paper and want it to build your application. Specify what Specifications you should provide to the Health-Dev framework to develop the code ideally. (15 points)
Answer: Heath-Dev is a model-based development framework that makes Sensor networks and Body sensor networks easier to understand. It will help with the interfacing complexities by generating code modules for the hardware components that I have to use in my project. So, I don't have to write low-level code for sensor data pr processing, health Dev uses AADL (Architecture Analysis and Design Language), which will help in executing code for hardware specifications like sensors, cameras, and smartphones, and it can also integrate external health devices. I just need to feed high-level specifications of the sensors I am using, and the type of data they are collecting (eg. heart rate, respiratory rate, etc). Health Dev will generate the appropriate modules to process this data, display the results to the user, and store them in the database.
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
Question 2	:In Project 1 you have stored the user’s symptoms data in the local server. Using the bHealthy application suite how can you provide feedback to the user and develop a novel application to improve context sensing and use that to generate the model of the user? (15 points)
Answer: The bHealthy application suite uses a physiological feedback-based system to track user wellness and give personalized advice according to physiological signals (e,g, accelerometer, etc.)which will help to improve the context-sensing application.
By using sensors like accelerometers, and heartrate monitors along with assessment and training applications bHealthy provides tools for physiological feedback to promote users’ wellness.
We can integrate the symptoms data and sensor readings ( heart rate, respiratory rate, nausea, diarrhea, etc) collected in the app with bHealthy's assessment tools to evaluate the user's health(e.g. stress or fatigue levels). This generates a dynamic user profile based on their symptoms and sensor data. The app can also provide personalized feedback, such as recommending relaxation exercises or health activities, based on the user's current context and health profile. Using bHealthy's training apps, exercise, and activities can be suggested to improve user's health. The application can also use machine learning to predict what future health trends will be and offer long-term insights based on the symptom pattern, helping users monitor their progress. By using bHealthy, the app will evolve from a simple symptom tracker to an intelligent health assistant that will give real-time feedback and personalized wellness recommendations which will improve user experience overall.
Question 3: A common assumption is mobile computing is mostly about app development. After completing Project 1 and reading both papers, have your views changed? If yes, what do you think mobile computing is about and why? If no, please explain why you still think mobile computing is mostly about app development, providing examples to support your viewpoint (10 points)
Answer: After completing Project 1 and insights from the papers, I think that mobile computing is a multifaceted field, involving much more than just creating apps. It involves a broad range of considerations beyond app development, for example.
Hardware Integration: We can integrate various hardware components like sensors, accelerometers, cameras, and GPS which is a critical part of mobile computing it can't be ignored while creating an app. In Project 1, I utilize a smartphone's camera, flash, and accelerometer to measure heart rate and respiratory rate.
Context Awareness and Data Processing: Collecting data from sensors and processing that data to provide context-specific feedback is an essential part of mobile computing. In the project and papers context awareness was the center of the design. 
Data Storage and Security: Storing symptoms data and health information collected on project 1 includes not just saving data to ROOM DB but also ensuring the data is secure and handled accordingly. As a result, considering privacy, encryption is important for mobile computing.
User Feedback and Interaction: In the bHealthy suite, feedback is important to improve user outcomes. It shows how important it is to consider designing mobile systems that not only collect data but also provide meaningful insights and personalized feedback to improve the user experience. Mobile computing is more than just making apps. It involves smart systems that work with users, combining hardware, data processing, privacy, and security. Projects like Health-Dev and bHealthy show how it takes a full system approach, connecting software, hardware, and users smoothly and safely.


