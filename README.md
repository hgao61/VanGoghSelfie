# VanGoghSelfie
##Introduction


This is the implementation of a picture visual effect post-processing Android app for demo. The current cellphone's compute power is limited by the ARM-CPU and the battery. To overcome this drawback, the post-processing of pictures that involves heavy computation will be send over to the network and use the GPU on the server to process it with pre-trained network and parameters.


The app's function is uploading a picture to network drive, taking the processed picture back and display it on the cellphone screen. The picture can be either from existing folder on the phone or from phone's camera. 


The server running pre-trained deep learning network will sense the uploaded file and start using the pre-trained network to process the picture and return the processed picture to the designated network folder.


The pre-trained network can be choose from 

##Notes
* The demo provides the framework of an Instagram style picture filter using the remote server's compute power. 
* With that goal in mind, the operation on the app side would be minimum to save on the cell phone's resources and battery.
* The framework can be expanded with more post-processing style by adding different pre-trained network. 
* To add more style, the app only need to add more option on the style selection menu, the change on the source code would be minimim.

   



##To-Do
