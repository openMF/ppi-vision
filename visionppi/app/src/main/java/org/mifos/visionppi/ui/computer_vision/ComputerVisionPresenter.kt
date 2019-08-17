package org.mifos.visionppi.ui.computer_vision

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.Toast
import org.mifos.visionppi.base.BasePresenter
import java.io.ByteArrayOutputStream
import org.mifos.visionppi.objects.*
import kotlin.collections.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ComputerVisionPresenter : BasePresenter<ComputerVisionMVPView>(){
    private var objectsDetected = ArrayList<String>()
    fun getObjects(images : ArrayList<Bitmap?>, context: Context) : ArrayList<String>{


        for (image in images){
            val imageByteArray = convertToByteArray(image)
            detectObjects(imageByteArray, context)
        }

        return objectsDetected
    }

    private fun detectObjects(imageByteArray: ByteArray, context: Context) {
        Log.i("...", "Analyzing")
        try {
            val imgString = Base64.encodeToString(imageByteArray, Base64.NO_WRAP)
            val requestBody = ModelRequestBody(PayloadRequest(ModelImage(imgString)))
            Log.i("dsad","calling")
            Network("https://automl.googleapis.com/v1beta1/",true).getRetrofitClient().create(Endpoint::class.java).classifyImage(requestBody).enqueue(object : Callback<PayloadResult> {

                override fun onResponse(call: Call<PayloadResult>?, response: Response<PayloadResult>?) {
                    if (response!!.isSuccessful) {
                        Log.d("Hello", response.body().toString())
                        ComputerVisionActivity.setObjects("${response?.body()?.items?.first()?.displayName}")
                        Toast.makeText(context, "Image Analysis Complete", Toast.LENGTH_LONG)
                    }
                }

                override fun onFailure(call: Call<PayloadResult>, t: Throwable) {
                    Log.i("Failed: ",t.message)
                }
            }
            )
            Log.d("WE","dsadsa")

        }catch (e : Exception)
        {
            Log.i("exception", e.toString())
        }
    }

    private fun convertToByteArray(image : Bitmap?) : ByteArray{
        val imageByteArray : ByteArray
        val stream = ByteArrayOutputStream()

        image?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        imageByteArray = stream.toByteArray()
        return imageByteArray
    }

}