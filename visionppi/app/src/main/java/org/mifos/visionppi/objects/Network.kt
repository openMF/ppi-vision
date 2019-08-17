package org.mifos.visionppi.objects


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory as GsonConverterFactory1

class Network(private val baseUrl: String, enableLog: Boolean) {


    private val sGson: Gson = GsonBuilder()
        .setLenient()
        .create()


    internal val okHttpClient: OkHttpClient

    private val gsonConverterFactory = GsonConverterFactory1.create(sGson)

    init {
        val httpClientBuilder = OkHttpClient.Builder()
            .readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.MINUTES)
            .connectTimeout(NetworkConfig.CONNECTION_TIMEOUT, TimeUnit.MINUTES)

        //Add debug interceptors
        if (enableLog) {
            httpClientBuilder.addInterceptor(
                HttpLoggingInterceptor()
                    .apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }

        val headerAuthorizationInterceptor = Interceptor() {
            var request = it.request()
            val headers = request.headers.newBuilder().add("Authorization","Bearer ya29.c.ElpnB2TbBnZuDEvLWDUEYpkyk6Bv6nD-en97MUSt8e1X7Rpdrikwuhp4A0c-p4E_klNKHKhSlsulo64WgpP7nvL0T6w5903KHYSMglHwwGEgjBnkYxRf_hx6U3A").build()
            request = request.newBuilder().headers(headers).build()
            return@Interceptor it.proceed(request)
        }

        httpClientBuilder.addInterceptor(headerAuthorizationInterceptor)

        okHttpClient = httpClientBuilder.build()
    }

    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

}

internal object NetworkConfig {


    internal const val READ_TIMEOUT = 1L

    internal const val WRITE_TIMEOUT = 1L

    internal const val CONNECTION_TIMEOUT = 1L
}

data class ModelRequestBody (@field:SerializedName("payload")val image: PayloadRequest)
data class PayloadRequest (@field:SerializedName("image")val image: ModelImage)
data class ModelImage (@field:SerializedName("imageBytes") val imageBytes : String)


internal interface Endpoint {

    @Headers("Content-Type: application/json")
    @POST("projects/152427287089/locations/us-central1/models/IOD5470105532557688832:predict")
    fun classifyImage(@Body body: ModelRequestBody): Call<PayloadResult>
}

data class PayloadResult(

    @field:SerializedName("payload")
    val items: List<PayLoad>
)

enum class Positivity {
    POSITIVITY_UNSPECIFIED, UNDECIDED, POSITIVE, NEGATIVE
}


data class NormalizedVertex(@field:SerializedName("x")
                            val x: Double,

                            @field:SerializedName("y")
                            val y: Double)

data class BoundingPoly(@field:SerializedName("normalizedVertices")
                        val normalizedVertices : List<NormalizedVertex>)


data class ImageObjectDetection(@field:SerializedName("boundingBox")
                                val boundingBox: BoundingPoly,

                                @field:SerializedName("score")
                                val score: Double,

                                @field:SerializedName("iouScore")
                                val iouScore: Double,

                                @field:SerializedName("createTimePositivity")
                                val createTimePositivity: Positivity)


data class PayLoad(@field:SerializedName("imageObjectDetection")
                   val imageObjectDetection: ImageObjectDetection,

                   @field:SerializedName("displayName")
                   val displayName: String)