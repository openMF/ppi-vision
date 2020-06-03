package org.mifos.visionppi.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import org.mifos.visionppi.R
import org.mifos.visionppi.objects.User

class PrefManager {

    val USER_DETAILS = "user_details"
    val USERNAME = "username"
    val PASSWORD = "password"
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

    fun saveLoginCredentials( username : String, password: String, context: Context, activity: Activity) {
        putString(USERNAME, username, activity, context)
        putString(PASSWORD, password, activity, context)
    }
}