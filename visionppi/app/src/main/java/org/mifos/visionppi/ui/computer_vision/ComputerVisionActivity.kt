package org.mifos.visionppi.ui.computer_vision

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.activity_computer_vision.analyse
import kotlinx.android.synthetic.main.activity_computer_vision.open_camera_btn
import kotlinx.android.synthetic.main.activity_computer_vision.res_list
import kotlinx.android.synthetic.main.activity_computer_vision.selected_images_list
import kotlinx.android.synthetic.main.activity_computer_vision.upload_from_gallery_btn
import kotlinx.android.synthetic.main.toolbar.*
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ObjectDetectionResultAdapter
import org.mifos.visionppi.adapters.SelectedImageAdapter

class ComputerVisionActivity : AppCompatActivity(), ComputerVisionMVPView {

    private val images = ArrayList<Bitmap?>()
    private val pickFromGallery = 1
    private val cameraRequest = 2
    private val myCameraPermissionCode = 100
    private val threshold = .6
    private val labelList: MutableList<String> = ArrayList()

    companion object {
        @JvmStatic
        var finalLabels: MutableList<List<String>> = ArrayList()
    }

    lateinit var localModel: LocalModel
    lateinit var customImageLabelerOptions: CustomImageLabelerOptions
    lateinit var imageLabeler: ImageLabeler
    var counter = 0
    var imageNos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_vision)

        setSupportActionBar(appToolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Analyse Images"

        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        upload_from_gallery_btn.setOnClickListener {
            fetchFromGallery()
        }

        open_camera_btn.setOnClickListener {
            fetchFromCamera()
        }

        selected_images_list.layoutManager = GridLayoutManager(this, 3)
        selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }

        analyse.setOnClickListener {
            counter = 0
            imageNos = images.size
            if (images.isNotEmpty()) {
                analyzeImages()
            } else {
                Toast.makeText(this, "Please select at least 1 image", Toast.LENGTH_SHORT).show()
            }
        }

        readLabels()
        initDetector()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
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
                    InputStreamReader(baseContext.assets.open("labels.txt")))

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

    override fun fetchFromGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(
                            this@ComputerVisionActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                        this@ComputerVisionActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        pickFromGallery
                )
            } else {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                val activitySelectIMage = 1234
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), activitySelectIMage)

                selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun fetchFromCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), myCameraPermissionCode)
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, cameraRequest)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun analyzeImages() {
        res_list.adapter = null
        for (image in images) {
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
                            ll.add("$text: $confidence")
                        }

                        Log.d("LABELS", "$text $confidence $index")
                    }
                    ++counter
                    finalLabels.add(ll)
                    if (counter == imageNos) {
                        res_list.adapter = ObjectDetectionResultAdapter(finalLabels, images, baseContext)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("ERROR", e.message!!)
                }
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1234 -> if (resultCode == Activity.RESULT_OK) {
                if (data?.data != null) {
                    val mImageUri: Uri = data.data!!
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mImageUri)
                    images.add(bitmap)
                    selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }
                } else {
                    if (data?.clipData != null) {
                        val mClipData: ClipData = data.clipData!!
                        for (i in 0 until mClipData.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val uri: Uri = item.uri
                            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                            images.add(bitmap)
                            selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }
                        }
                    }
                }
            }
        }

        if (requestCode === cameraRequest && resultCode === Activity.RESULT_OK) {
            val photoCaptured = data?.extras?.get("data") as Bitmap
            images.add(photoCaptured)
            selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            pickFromGallery ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, pickFromGallery)
                } else {
                    showToastMessage(getString(R.string.cant_open_gallery))
                }
        }

        if (requestCode === myCameraPermissionCode) {
            if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                showToastMessage("Camera permission granted")
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, cameraRequest)
            } else {
                showToastMessage("Camera permission denied")
            }
        }
    }

    private fun imageRemove(position: Int) {
        images.removeAt(position)
        selected_images_list.adapter = SelectedImageAdapter(images, this) { position: Int -> imageRemove(position) }
    }
}
