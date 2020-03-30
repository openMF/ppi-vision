package org.mifos.visionppi.ui.home

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import khttp.get
import khttp.structures.authorization.BasicAuthorization
import org.json.JSONArray
import org.mifos.visionppi.R
import org.mifos.visionppi.api.APIEndPoint
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.objects.Client
import org.mifos.visionppi.utils.PrefManager

/**
 * Created by Apoorva M K on 27/06/19.
 */

class MainPresenter : BasePresenter<MainMVPView>() {


    fun searchClients(query: String, context:Context, activity: Activity) : MutableList<Client> {

        var searchResult : MutableList<Client> = mutableListOf<Client>()
        val url = context.getString(R.string.demoURL).plus(APIEndPoint.SEARCH)
        val tenantId  = context.getString(R.string.tenantId)
        val contentType  = context.getString(R.string.contentType)

        val payload = mapOf("query" to query, "exactMatch" to "false")
        val header = mapOf(
            "Fineract-Platform-TenantId" to tenantId)
        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name),Context.MODE_PRIVATE)
        val mPrefManager = PrefManager()
        val username = sharedPref.getString(mPrefManager.USERNAME, " ")
        val password = sharedPref.getString(mPrefManager.PASSWORD, " ")


        try {
            val response = get(url = url, params = payload, headers = header, auth= BasicAuthorization(username!!, password!!))

            if (response.statusCode == 200) {

                val obj: JSONArray = response.jsonArray
                var gson: Gson = Gson()

                for ( i in 0..(obj.length()-1))
                {
                    Log.i("i",i.toString())
                    val boo = searchResult.add(gson.fromJson(obj.getJSONObject(i).toString(),Client::class.java))
                }

            }
        }catch (e : Exception)
        {
            Log.i("exception", e.toString())
        }

        return searchResult
    }
}