<img height='175' src="https://user-images.githubusercontent.com/37406965/51083189-d5dc3a80-173b-11e9-8ca0-28015e0893ac.png" align="left" hspace="1" vspace="1">

# PPI-Vision :iphone:

Vision PPI is a computer vision and machine learning based android app designed to help in filling out the PPI Survey. There are two broad aspects to this project - The android app which provides the interface to the field officer for conducting the survey and the machine learning models which are used in the backend of app to analyze the images captured. 


## Wiki 📙
For more information about the usecases of the project and details about PPI, the API used and demo credentials, please take a look at the [project wiki](https://github.com/openMF/ppi-vision/wiki). 

## Screenshots :camera:
![Untitled design (5)](https://user-images.githubusercontent.com/75531664/189319200-f7f3b143-2757-46c8-9342-0021fada2ca2.png)

![Untitled design (7)](https://user-images.githubusercontent.com/75531664/189319225-8d40e34d-1be2-4743-92e9-2be54eac336b.png)

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

## Usage and Flow of Vison PPI app :ocean:

1. After installing or building the app, the first screen which is encountered is a Login screen with ppi logo on top.To log into the app you need to enter **username: mifos** and **password : password** in the respectative columns.


2. The second screen renders the home page. A search bar is present where client account can be searched by typing their names. At the top of screen Share, Logout and About Us button are present that works to share this app , logout from the app and to go to about us page respectatively. 

3. The third screen shows up with analyze images page.There are more than one open available for seleting the image, image can be selected from gallary or directly form the camera depending upon the user prefrerence. A screen also appers to ask for premision form the user for accesing storage and camera. Which are needed for getting the images from gallery and directly from camera. 

4. After clicking on Analyse image button the result of modle appears in the Object Deteciton Result column. Only that result is shown for which modle has an confidance of more than 60 %.

5. The last screen appears with the respectative PPI questions. THe quesion are not stored in the app but directly comming from the API. User can tick only one choice out of the give. The scores are written for each choice which are added at the end and be used in calculating the over all survey score. 


6. About Us page features information about the app. Links for github and twitter account of mifos also given in this page. At the bottom 
**Useful Resources for getting started with MLKIt if you are interested in contributing to this project:**

- [Label images with ML Kit on Android](https://developers.google.com/ml-kit/vision/image-labeling/android)
- [MLKIt Quickstart Sample](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart)
- [MLKit Custom Models](https://developers.google.com/ml-kit/custom-models)
- [Labelling images with a custom model on Android](https://developers.google.com/ml-kit/vision/image-labeling/custom-models/android)
- [Converting an existing TensorFlow object detection model into a TensorFlow Lite model](https://www.tensorflow.org/lite/convert)

### License :page_with_curl:

This project adhers to the [MIT License](https://github.com/openMF/ppi-vision/blob/master/LICENSE).
