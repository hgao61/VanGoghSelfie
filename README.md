# VanGoghSelfie


## Introduction


This is the implementation of a picture visual effect post-processing framework Android app for demo. The current cellphone's compute power is limited by the ARM-CPU and the battery. To overcome this drawback, the post-processing of pictures that involves heavy computation will be sent over to the network and use the GPU on the server to process it with pre-trained network and parameters.


The app's main function is uploading a picture to network drive(server), taking the processed picture back and displaying it on the cellphone screen. The picture can be either from existing folder on the phone or from phone's camera. 


The server running pre-trained deep learning network will sense the uploaded file and start using the pre-trained network to process the picture and return the processed picture to the designated network folder.


## Notes
* The demo provides the framework of an Instagram style picture filter using the remote server's compute power. 
* With that goal in mind, the operation on the app side would be minimum to save on the cell phone's os system resources and battery.
* The framework can be expanded with more post-processing style by adding more pre-trained networks. 
* To add more style, the app only need to add more option on the style selection menu, the change on the source code would be minimum.
* Currently there are two pre-trained network available. One is apply the picture with Van Gogh style.The network is pre-trained with neural style in TensorFlow. The network is using exiting source code by Anish Athalye online. The other neural network is a picture classication network detecting what type of flower the picture is, the network is pretrained to identify 100 different types of flowers.  

## To-Do
* Expand the current 2 pre-trained network to more networks. 
* Fine tuning the network to be more efficient, reducing the parameters and trading off between accuracy over compute time.  
* Improve on the UI interface.
