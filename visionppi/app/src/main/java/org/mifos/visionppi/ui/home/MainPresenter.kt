package org.mifos.visionppi.ui.home

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import khttp.get
import org.json.JSONArray
import org.mifos.visionppi.R
import org.mifos.visionppi.api.ApiEndPoints
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.ui.user_profile.UserProfilePresenter
import org.mifos.visionppi.utils.AuthKey

/**
 * Created by Apoorva M K on 27/06/19.
 */

class MainPresenter : BasePresenter<MainMVPView>() {

    var mUserProfilePresenter: UserProfilePresenter = UserProfilePresenter()

    fun searchClients(query: String, context: Context, activity: FragmentActivity): MutableList<Client> {

        // val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        // StrictMode.setThreadPolicy(policy)

        val user = mUserProfilePresenter.fetchUserDetails(activity, context)
        val searchResult: MutableList<Client> = mutableListOf<Client>()
        val url = context.getString(R.string.demoURL).plus(ApiEndPoints.SEARCH)
        val tenantId = "mobile"
        val contentType = context.getString(R.string.contentType)

        val payload = mapOf("query" to query, "exactMatch" to "false")
        val header = mapOf(
            "Fineract-Platform-TenantId" to tenantId,
            "Authorization" to "Basic bWlmb3M6cGFzc3dvcmQ=")

        try {

            val response = get(url = url, params = payload, headers = header, auth = AuthKey((user.base64EncodedAuthenticationKey).toString()))
            response.request.run {
                println("headers $headers")
                println("url ${this.url}")
                println("params ${this.params}")
                println("auth ${this.auth}")
            }

            if (response.statusCode == 200) {

                val obj: JSONArray = response.jsonArray
                val gson: Gson = Gson()

                for (i in 0 until obj.length()) {
                    Log.i("i", i.toString())
                    val boo = searchResult.add(gson.fromJson(obj.getJSONObject(i).toString(), Client::class.java))
                }
            }
        } catch (e: Exception) {
            Log.i("exception", e.toString())
        }

        return searchResult
    }
}
