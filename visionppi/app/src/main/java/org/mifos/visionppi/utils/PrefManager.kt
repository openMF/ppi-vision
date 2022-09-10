package org.mifos.visionppi.utils

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import org.mifos.visionppi.R
import org.mifos.visionppi.models.User

/**
 * @author HARSH-nith
 * @since 13/07/2022
 */

class PrefManager {

    val userDetails = "user_details"
    private val username = "username"
    private val password = "password"
    private val gson: Gson = Gson()

    private fun putString(preferenceKey: String, preferenceValue: String, activity: Activity, context: Context) {

        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString(preferenceKey, preferenceValue)
            apply()
        }
    }

    fun saveUser(user: User, context: Context, activity: Activity) {
        putString(userDetails, gson.toJson(user), activity, context)
    }

    fun saveLoginCredentials(username: String, password: String, context: Context, activity: Activity) {
        putString(this.username, username, activity, context)
        putString(this.password, password, activity, context)
    }

    fun clear(activity: Activity, context: Context) {
        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        val editor = sharedPref.edit()
        // prevent deletion of url and tenant
        for ((key) in sharedPref.all) {
            editor.remove(key)
        }
        editor.apply()
    }
}
