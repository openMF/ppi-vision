package org.mifos.visionppi.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import khttp.post
import org.json.JSONObject
import org.mifos.visionppi.R
import org.mifos.visionppi.R.*
import org.mifos.visionppi.api.APIEndPoint
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.objects.User
import org.mifos.visionppi.utils.PrefManager

/**
 * Created by Apoorva M K on 25/06/19.
 */

class LoginPresenter : BasePresenter<LoginMVPView>(){

    fun login(username: String, password: String, context: Context, activity: Activity) : Boolean {

        val url = context.getString(string.demoURL).plus(APIEndPoint.AUTHENTICATION)
        val tenantId  = context.getString(string.tenantId)
        val contentType  = context.getString(string.contentType)

        val payload = mapOf("username" to username, "password" to password)
        val header = mapOf("Content-Type" to contentType,
                                            "Fineract-Platform-TenantId" to tenantId)

        try {
            val response = post(url = url, params = payload, headers = header)

            if (response.statusCode == 200) {

                val obj: JSONObject = response.jsonObject
                var gson: Gson = Gson()
                var mPrefManager = PrefManager()
                var jsonString = obj.toString()
                var user = gson.fromJson(jsonString, User::class.java)
                mPrefManager.saveUser(user, context, activity)
                mPrefManager.saveLoginCredentials(username, password, context, activity)

                return true
            }
        }catch (e : Exception)
        {
            Log.i(context.getString(R.string.exception),e.toString())
            return false
        }
        return false

    }

}


