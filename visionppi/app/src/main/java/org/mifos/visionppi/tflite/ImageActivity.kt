package org.mifos.visionppi.tflite

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxkotlin.subscribeBy
import java.io.FileNotFoundException

class ImageActivity : AppCompatActivity() {
    private val CHOOSE_IMAGE = 1001
    private lateinit var photoImage: Bitmap
    private lateinit var classifier: ImageClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_image)
        classifier = ImageClassifier(getAssets())
        //imageResult.setOnClickListener {
            //choosePicture()
        //}
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, CHOOSE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK)
            try {
                //val stream = contentResolver!!.openInputStream(data.getData())
                //if (::photoImage.isInitialized) photoImage.recycle()
                //photoImage = BitmapFactory.decodeStream(stream)
                //photoImage = Bitmap.createScaledBitmap(photoImage, INPUT_SIZE, INPUT_SIZE, false)
                //imageResult.setImageBitmap(photoImage)
                classifier.recognizeImage(photoImage).subscribeBy(
                    onSuccess = {

                        //txtResult.text = it.toString()
                    }
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }
}