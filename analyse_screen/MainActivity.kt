package com.example.analyse_screen

import android.Manifest.permission.CAMERA
import android.Manifest.permission_group.CAMERA
import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.media.MediaRecorder.VideoSource.CAMERA
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



class MainActivity : AppCompatActivity() {



    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2



    private lateinit var imageView: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        imageView = findViewById(R.id.imageView2)
        val cameraButton = findViewById<ImageView>(R.id.cameraButton)
        val galleryButton = findViewById<ImageView>(R.id.galleryButton)
        val analyseButton = findViewById<Button>(R.id.analyseButton)



        // Check camera permission before opening the camera
        cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with camera operation
                dispatchTakePictureIntent()
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            }
        }



        galleryButton.setOnClickListener {
            dispatchGalleryIntent()
        }



        analyseButton.setOnClickListener {
            onAnalysisButtonClick()
        }
    }



    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }



    private fun dispatchGalleryIntent() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { galleryIntent ->
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        // Save the full-size image to a file
                        val imageFile = saveImageToFile(imageBitmap)



                        // Load and display the full-size image into the ImageView
                        if (imageFile != null) {
                            val fullSizeBitmap = BitmapFactory.decodeFile(imageFile.path)
                            imageView.setImageBitmap(fullSizeBitmap)
                        } else {
                            // Handle the case when the image couldn't be saved or loaded
                        }
                    } else {
                        // Handle the case when imageBitmap is null (e.g., user canceled the capture)
                    }
                }
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri = data?.data
                    val imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    imageView.setImageBitmap(imageBitmap)
                }
            }
        }
    }



    private fun saveImageToFile(imageBitmap: Bitmap): File? {
        return try {
            // Create a file to save the image
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )



            // Save the bitmap to the file
            val outputStream = FileOutputStream(imageFile)
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()



            imageFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }




    private fun onAnalysisButtonClick() {
        println("Analyse")
    }



    private fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }




    }
