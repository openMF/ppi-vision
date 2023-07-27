package org.mifos.visionppi.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ActivityAnalyzeBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class AnalyzeActvity : Fragment() {
    private lateinit var activityAnalyzeBinding: ActivityAnalyzeBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2

    private lateinit var imageView: ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        activityAnalyzeBinding=ActivityAnalyzeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        imageView = activityAnalyzeBinding.imageView2

        imageView.setImageResource(R.drawable.mifos_logo);

        activityAnalyzeBinding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, proceed with camera operation
                dispatchTakePictureIntent()
            } else {
                // Request camera permission
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            }
        }

        activityAnalyzeBinding.galleryButton.setOnClickListener {
            dispatchGalleryIntent()
        }

        activityAnalyzeBinding.analyseButton.setOnClickListener {
            onAnalysisButtonClick()
        }

        return activityAnalyzeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun dispatchTakePictureIntent() {
        val packageManager = requireActivity().packageManager
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
                    val imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                    imageView.setImageBitmap(imageBitmap)
                }
            }
        }
    }

    private fun saveImageToFile(imageBitmap: Bitmap): File? {
        return try {
            // Create a file to save the image
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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

        val surveyIntent = Intent(requireContext(), SurveyActivity::class.java)
        startActivity(surveyIntent)

    }

//    private fun openFragment(fragment: Fragment) {
//        val transaction: FragmentTransaction = getParentFragmentManager().beginTransaction()
//        transaction.replace(R.id.fragmentContainer, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

    fun showToastMessage(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

}
