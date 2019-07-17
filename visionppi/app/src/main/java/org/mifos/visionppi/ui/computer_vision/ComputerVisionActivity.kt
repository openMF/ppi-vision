package org.mifos.visionppi.ui.computer_vision

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import org.mifos.visionppi.R
import android.graphics.BitmapFactory
import android.app.Activity
import kotlinx.android.synthetic.main.activity_computer_vision.*
import android.content.pm.PackageManager
import androidx.recyclerview.widget.GridLayoutManager
import org.mifos.visionppi.adapters.SelectedImageAdapter


class ComputerVisionActivity : AppCompatActivity(), ComputerVisionMVPView {


    private var images = ArrayList<Bitmap?>()
    private val PICK_FROM_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_vision)

        upload_from_gallery_btn.setOnClickListener {
            fetchFromGallery()
        }

        selected_images_list.layoutManager = GridLayoutManager(this, 3)
        selected_images_list.adapter = SelectedImageAdapter(images, this)
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
                    PICK_FROM_GALLERY
                )
            } else {
                val i = Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI

                )
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                val ACTIVITY_SELECT_IMAGE = 1234
                startActivityForResult(i, ACTIVITY_SELECT_IMAGE)

                showToastMessage("The number of images ="+images.size.toString())
                selected_images_list.adapter = SelectedImageAdapter(images, this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun fetchFromCamera() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun analyzeImages() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1234 -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()


                val yourSelectedImage = BitmapFactory.decodeFile(filePath)
                images.add(yourSelectedImage)

            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PICK_FROM_GALLERY ->
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY)
                } else {
                    showToastMessage("Cannot open gallery")
                }
        }
    }

}
