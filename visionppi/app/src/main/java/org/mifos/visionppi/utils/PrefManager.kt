package org.mifos.visionppi.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import org.mifos.visionppi.R
import org.mifos.visionppi.objects.User

class PrefManager {

    public val USER_DETAILS = "user_details"
    private val gson :Gson = Gson()

    private fun putString(preferenceKey :String, preferenceValue: String, activity:Activity, context: Context){

        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name),Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString(preferenceKey, preferenceValue)
            apply()
        }

    }


    fun saveUser(user: User, context: Context, activity: Activity){
        putString(USER_DETAILS, gson.toJson(user), activity, context)
    }
}