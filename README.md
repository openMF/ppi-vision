<img height='175' src="https://user-images.githubusercontent.com/37406965/51083189-d5dc3a80-173b-11e9-8ca0-28015e0893ac.png" align="left" hspace="1" vspace="1">

# PPI-Vision :iphone:

Vision PPI is a computer vision and machine learning based android app designed to help in filling out the PPI Survey. There are two broad aspects to this project - The android app which provides the interface to the field officer for conducting the survey and the machine learning models which are used in the backend of app to analyze the images captured. 


## Wiki 📙
For more information about the usecases of the project and details about PPI, the API used and demo credentials, please take a look at the [project wiki](https://github.com/openMF/ppi-vision/wiki). 

## Screenshots :camera:
![Untitled design (5)](https://user-images.githubusercontent.com/75531664/189319200-f7f3b143-2757-46c8-9342-0021fada2ca2.png)

![Untitled design (7)](https://user-images.githubusercontent.com/75531664/189319225-8d40e34d-1be2-4743-92e9-2be54eac336b.png)

## Usage and Flow of Vison PPI app :ocean:

1. After installing or building the app, the first screen which is encountered is a Login screen with ppi logo on top of it. To log into the app enter the **username: mifos** and **password : password** in the respectative columns of username and password.

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365458-1bd81a44-616a-4b71-a8cf-cca93bc18d42.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189365531-2aab2254-3771-400d-92d6-b08a88bb23d2.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


2. The second screen renders the home page. It contains search bar through which client account can be searched by typing their names. At the top of screen Share, Logout and About Us button are present they functions to share the app ,logout from the app and to go to about us page respectively. 

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365588-a3159e9b-9479-49b9-96ae-8b5c2039e503.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp</p>


3. The third screen comes up with analyze images page. It contains option for seleting the image from gallary or directly or from capturing directly for the camera. Previously to selecting an image an screen appers to ask for premision from user for accesing the gallery and camera

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365731-49683277-b70b-4f5f-bf29-16b94cd298a2.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189365739-9a9712cb-1ed0-4455-9f39-58d8bae77277.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


4. The result of object detection model appears in the Object Deteciton Result column after clicking on analyse images button. Result threshold is applied on object detection result,that result is shown up only those outcomes for which model has an confidance of 60 % or more than it.

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365822-9d8064fa-a7c1-496e-b6be-660f2a818a39.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189365846-aa711066-83f1-4515-9b85-f53807dcad83.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


5. The fifth screen contains PPI Questions. These quesion are directly comming from the API not form app. Users are able to tick only one choice out of the given. The scores are written for each question choice which are added at the end to in calculating over all PPI survey score. 

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365905-c53b0659-46cd-468c-b0dc-aab8ec290ea9.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189365886-371f75a6-cbc8-4863-864a-a3894e810f49.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>

6. About Us page features information about the app. Links for github and twitter account of mifos is given in this page. Contact details and current app verion are shown here that helps to reach out to community whenever needed. 

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189365993-3be8336f-62be-4db9-8e71-200cf641c337.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp</p>


## Contributions :information_desk_person:

We welcome contributions in form of issues, as well as pull requests. Please go though the contribution guidelines over [here](https://github.com/openMF/ppi-vision/blob/master/CONTRIBUTING.md) and also through our Code of Conduct [here](https://github.com/openMF/ppi-vision/blob/master/CODE_OF_CONDUCT.md).

## Installation ⚙
* The app can be installed by directly downloading the Android Application Package (APK) File located at : "give drive link here " into the Mobile phone device with Android OS.
* For building the project from on your local machine, clone the repo and Open it with Android Studio and the follow the development setup guidlines given below.

## Development Setup :triangular_ruler:

Before you begin, you should have already downloaded the Android Studio SDK and set it up correctly. You can find a guide on how to do this here: [Setting up Android Studio](http://developer.android.com/sdk/installing/index.html?pkg=studio)

### Setting up the Android Project :black_nib:

1. Download the project source. You can do this either by forking and cloning the repository (recommended if you plan on pushing changes) or by downloading it as a ZIP file and extracting it.

2. Install the NDK in Android Studio.

3. Open Android Studio, you will see a **Welcome to Android** window. Under Quick Start, select *Import Project (Eclipse ADT, Gradle, etc.)*

4. Navigate to the directory where you saved the ppi-vision project, select the root folder of the project (the folder named "vision-ppi"), and hit OK. Android Studio should now begin building the project with Gradle.

5. Once this process is complete and Android Studio opens, check the Console for any build errors.

    - *Note:* If you receive a Gradle sync error titled, "failed to find ...", you should click on the link below the error message (if available) that says *Install missing platform(s) and sync project* and allow Android studio to fetch you what is missing.

6. To Build the app, go to *Build > Make Project* (or alternatively press the Make Project icon in the toolbar).

7. If the app was built successfully, you can test it by running it on either a real device or an emulated one by going to *Run > Run 'app'* or pressing the Run icon in the toolbar.

### Image Labelling :framed_picture:

#### Data Collection 

For data collection, [this](https://github.com/hardikvasa/google-images-download) tool can be used. In order to increase the size of the dataset, we have used image augmentation techniques present in [GeneratedImages.py](https://github.com/openMF/ppi-vision/blob/master/DataCollection/GenerateImages.py). You can see how each technique works [here](https://github.com/openMF/ppi-vision/blob/master/DataCollection/ImageAugmentation.ipynb) in the sample notebook. 

#### Model Training

We use [Google's MLKit SDK](https://developers.google.com/ml-kit) for image labelling. In order to train the model used further, we use transfer learning on our collected data. 



**Useful Resources for getting started with MLKIt if you are interested in contributing to this project:**

- [Label images with ML Kit on Android](https://developers.google.com/ml-kit/vision/image-labeling/android)
- [MLKIt Quickstart Sample](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart)
- [MLKit Custom Models](https://developers.google.com/ml-kit/custom-models)
- [Labelling images with a custom model on Android](https://developers.google.com/ml-kit/vision/image-labeling/custom-models/android)
- [Converting an existing TensorFlow object detection model into a TensorFlow Lite model](https://www.tensorflow.org/lite/convert)

### License :page_with_curl:

This project adhers to the [MIT License](https://github.com/openMF/ppi-vision/blob/master/LICENSE).
