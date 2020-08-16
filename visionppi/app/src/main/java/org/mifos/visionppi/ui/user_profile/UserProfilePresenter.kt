package org.mifos.visionppi.ui.user_profile

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import org.mifos.visionppi.R
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.models.User
import org.mifos.visionppi.utils.PrefManager

/**
 * Created by Apoorva M K on 27/06/19.
 */

class UserProfilePresenter : BasePresenter<UserProfileMVPView>(){

    private val gson : Gson = Gson()

    fun fetchUserDetails(activity: Activity, context: Context): User {
        val mPrefManager = PrefManager()
        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name),Context.MODE_PRIVATE)
        val userJsonString = sharedPref.getString(mPrefManager.USER_DETAILS, " ")

        return gson.fromJson(userJsonString, User::class.java)

    }
}