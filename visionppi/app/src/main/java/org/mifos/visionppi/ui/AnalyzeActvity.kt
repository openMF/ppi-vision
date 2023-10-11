package org.mifos.visionppi.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import org.mifos.visionppi.adapters.ObjectDetectionGridAdapter
import org.mifos.visionppi.databinding.ActivityAnalyzeBinding
import org.mifos.visionppi.objects.Client
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class AnalyzeActvity : Fragment() {
    private lateinit var activityAnalyzeBinding: ActivityAnalyzeBinding
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_GALLERY = 2
    private val MAX_SELECTED_IMAGES = 9 // Maximum number of images to select
    private var totalSelectedImages = 0

    private lateinit var imageGridView: GridView
    private lateinit var imageAdapter: ImageAdapter
    private val selectedImages = mutableListOf<Bitmap>()

    var counter = 0
    var imageNos = 0

    companion object {
        @JvmStatic
        var finalLabels: MutableList<List<String>> = ArrayList()
    }

    lateinit var localModel: LocalModel
    lateinit var customImageLabelerOptions: CustomImageLabelerOptions
    lateinit var imageLabeler: ImageLabeler
    private val labelList: MutableList<String> = ArrayList()
    private val threshold = .6

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        activityAnalyzeBinding=ActivityAnalyzeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        val clientDetails = arguments?.getParcelable<Client>("clientDetails")

        if(clientDetails != null){
            activityAnalyzeBinding.imageView.text =clientDetails?.entityName
        }

        imageGridView = activityAnalyzeBinding.imageGridView
        imageAdapter = ImageAdapter(requireContext(), selectedImages)

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

        readLabels()
        initDetector()

        activityAnalyzeBinding.analyseButton.setOnClickListener {
            onAnalysisButtonClick()
        }

        return activityAnalyzeBinding.root
    }

    private fun dispatchTakePictureIntent() {
        if (totalSelectedImages >= MAX_SELECTED_IMAGES) {
            // Display a message to the user that the maximum limit is reached
            Toast.makeText(requireContext(), "Maximum image limit reached", Toast.LENGTH_SHORT).show()
            return
        }

        val packageManager = requireActivity().packageManager
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun dispatchGalleryIntent() {
        val remainingSlots = MAX_SELECTED_IMAGES - totalSelectedImages
        if (remainingSlots <= 0) {
            // Display a message to the user that the maximum limit is reached
            Toast.makeText(requireContext(), "Maximum image limit reached", Toast.LENGTH_SHORT).show()
            return
        }

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Enable multiple image selection
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    if (imageBitmap != null && totalSelectedImages < 12) {
                        selectedImages.add(imageBitmap)
                        totalSelectedImages++
                        imageAdapter.notifyDataSetChanged()
                    }
                }
                REQUEST_IMAGE_GALLERY -> {
                    val clipData = data?.clipData
                    if (clipData != null) {
                        val remainingSlots = MAX_SELECTED_IMAGES - totalSelectedImages
                        val itemsToAdd = clipData.itemCount.coerceAtMost(remainingSlots)
                        for (i in 0 until itemsToAdd) {
                            val imageUri = clipData.getItemAt(i).uri
                            val imageBitmap = MediaStore.Images.Media.getBitmap(
                                    requireContext().contentResolver, imageUri
                            )
                            selectedImages.add(imageBitmap)
                            totalSelectedImages++
                        }
                        imageAdapter.notifyDataSetChanged()
                    } else if (data?.data != null && totalSelectedImages < 12) {
                        val imageUri = data.data
                        val imageBitmap = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imageUri
                        )
                        selectedImages.add(imageBitmap)
                        totalSelectedImages++
                        imageAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun initDetector() {
        localModel =
                LocalModel.Builder()
                        .setAssetFilePath("mlmodel.tflite")
                        .build()

        customImageLabelerOptions =
                CustomImageLabelerOptions.Builder(localModel)
                        .setMaxResultCount(10)
                        .build()

        imageLabeler =
                ImageLabeling.getClient(customImageLabelerOptions)
    }

    private fun readLabels() {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(
                    InputStreamReader(requireContext().assets.open("labels.txt")))

            while (reader.readLine().also { if (it != null) labelList.add(it) } != null) {
            }
        } catch (e: IOException) {
            Log.e("ERROR", e.toString())
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e("ERROR", e.toString())
                }
            }
        }
    }

    private fun onAnalysisButtonClick() 
        imageGridView.adapter = imageAdapter
        finalLabels.clear()
        counter = 0
        imageNos = selectedImages.size
        if (selectedImages.isNotEmpty()) {
            analyzeImages()
        } else {
            Toast.makeText(requireContext(), "Please Select At Least 1 Image", Toast.LENGTH_SHORT).show()
            return
        }

        val surveyIntent = Intent(requireContext(), SurveyActivity::class.java)
        startActivity(surveyIntent)
    }

    fun analyzeImages() {
        imageGridView.adapter = null
        for (image in selectedImages) {
            detect(image!!)
        }
    }

    private fun detect(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val ll: MutableList<String> = ArrayList()

        imageLabeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = labelList[label.index]
                        val confidence = label.confidence
                        val index = label.index
                        if (confidence > threshold) {
                            ll.add("$text : $confidence")
                        }

                        Log.d("LABELS", "$text $confidence $index")
                    }
                    ++counter
                    finalLabels.add(ll)
                    if (counter == imageNos) {
                        imageGridView.adapter = ObjectDetectionGridAdapter(finalLabels, selectedImages, requireContext())
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("ERROR", e.message!!)
                }
    }

    private inner class ImageAdapter(private val context: Context, private val images: List<Bitmap>) : BaseAdapter() {
        override fun getCount(): Int = images.size
        override fun getItem(position: Int): Any = images[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val imageView: ImageView
            if (convertView == null) {
                imageView = ImageView(context)
                imageView.layoutParams = AbsListView.LayoutParams(200, 200)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                imageView = convertView as ImageView
            }

            imageView.setImageBitmap(images[position])
            return imageView
        }
    }
}
