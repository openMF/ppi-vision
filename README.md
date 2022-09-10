<img height='175' src="https://user-images.githubusercontent.com/37406965/51083189-d5dc3a80-173b-11e9-8ca0-28015e0893ac.png" align="left" hspace="1" vspace="1">

# PPI-Vision :iphone:

Vision PPI is a computer vision and machine learning based android app designed to help in filling out the PPI Survey. There are two broad aspects to this project - The android app which provides the interface to the field officer for conducting the survey and the machine learning models which are used in the backend of app to analyze the images captured. 


## Wiki 📙
For more information about the usecases of the project and details about PPI, the API used and demo credentials, please take a look at the [project wiki](https://github.com/openMF/ppi-vision/wiki). 

## Screenshots :camera:
![Untitled design (5)](https://user-images.githubusercontent.com/75531664/189319200-f7f3b143-2757-46c8-9342-0021fada2ca2.png)

![Untitled design (7)](https://user-images.githubusercontent.com/75531664/189319225-8d40e34d-1be2-4743-92e9-2be54eac336b.png)

## Usage and Flow of Vision PPI app :ocean:

1. After installing or building the app, the first screen which is encountered is a **Login screen** with ppi logo on top of it. To log into the app enter the `username = mifos` and `password = password` in the respectative columns of username and password.

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189433266-ede5ab7b-2b77-4beb-98f4-1819ee1baf2b.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189433324-6bdbeaa8-1115-466f-befc-0b47d39768e4.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


2. The second screen renders the **Home page**. It contains search bar through which client account can be searched by typing their names. At the top of screen *Share, Logout and About Us* buttons are present they functions to share the app ,logout from the app and to go to about us page respectively. 

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189433442-878f655d-ecc3-4114-8add-ff87baf63b10.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp</p>


3. The third screen comes up with **Analyze Images page**. It contains option for seleting the image from gallary or directly capturing form the camera. An other screen also appears here that asks for premission from user for accesing the gallery and camera.

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189433760-dbdc4e6d-8dad-42e9-9926-72d3fe2dc019.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189433861-f00145ee-e162-44f1-bb56-903818cd884d.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


4. The result of object detection model appears in the Object Deteciton Result column after clicking on analyse images button. *Result threshold* implemented results in shown up only those outcomes for which model has an confidence of 60 % or more .

<p align=center><img src="https://user-images.githubusercontent.com/75531664/189434050-8988a1d1-b132-45d2-8603-9a7c9596556d.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189433924-5ad9a677-13e7-46f4-8ec3-ab1f558058d7.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>


5. The fifth screen contains **PPI Questions**. These quesion are directly comming from the API not from app. Users are able to tick only one choice out of the given. The scores are written for each question choice which are added at the end to in calculating over all PPI survey score. 


<p align=center><img src="https://user-images.githubusercontent.com/75531664/189434123-9d319098-4be8-4335-8ddc-06694fcb1853.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp<img src="https://user-images.githubusercontent.com/75531664/189434142-3f933b39-a3fa-4016-8a3f-e9bfaa636a6a.jpg" width="200" height="450" > &nbsp&nbsp&nbsp&nbsp&nbsp </p>

6. **About Us** page features information about the app. Links for `github and twitter` account of mifos is given in this page. Contact details and current app version are shown here . Contact details helps to reach out to community whenever needed. 


<p align=center><img src="https://user-images.githubusercontent.com/75531664/189434305-34f2b110-fef8-43f4-b531-839e0e85725e.jpg" width="200" height="450" >&nbsp&nbsp&nbsp&nbsp&nbsp</p>


## Contributions :information_desk_person:

We welcome contributions in form of issues, as well as pull requests. Please go though the contribution guidelines over [here](https://github.com/openMF/ppi-vision/blob/master/CONTRIBUTING.md) and also through our Code of Conduct [here](https://github.com/openMF/ppi-vision/blob/master/CODE_OF_CONDUCT.md).

## Installation ⚙
* The app can be installed by directly downloading the Android Application Package (APK)  into the Mobile phone device with Android OS.
**[Download the App](https://drive.google.com/drive/folders/1PW-EVUaxE1AuEK-VOC7hU70RM_IB2rU6?usp=sharing)**
* For building the project from on your local machine, clone the repo and Open it with Android Studio and the follow the development setup guidlines given below.

## Development Setup :triangular_ruler:

## Alert ! ⚠️
The current apk is built using **Android Studio Chipmunk | 2021.2.1** version.

*Follow this [Link](https://developer.android.com/studio/archive.html) to download specific version.*

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

We use [Google's ML Kit SDK](https://developers.google.com/ml-kit) for image labelling. In order to train the model used further, we use transfer learning on our collected data. 

**Useful resources for getting started with Transfer Learning**

- [Transfer learning and fine-tuning](https://www.tensorflow.org/tutorials/images/transfer_learning)
- [Transfer learning with TensorFlow Hub](https://www.tensorflow.org/tutorials/images/transfer_learning_with_hub)

**Useful Resources for getting started with ML Kit if you are interested in contributing to this project:**

- [Label images with ML Kit on Android](https://developers.google.com/ml-kit/vision/image-labeling/android)
- [MLKIt Quickstart Sample](https://github.com/googlesamples/mlkit/tree/master/android/vision-quickstart)
- [MLKit Custom Models](https://developers.google.com/ml-kit/custom-models)
- [Labelling images with a custom model on Android](https://developers.google.com/ml-kit/vision/image-labeling/custom-models/android)
- [Converting an existing TensorFlow object detection model into a TensorFlow Lite model](https://www.tensorflow.org/lite/convert)

### License :page_with_curl:

This project adhers to the [MIT License](https://github.com/openMF/ppi-vision/blob/master/LICENSE).
