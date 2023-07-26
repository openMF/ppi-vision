package org.mifos.visionppi.ui.menu
import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import org.mifos.visionppi.R
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.models.User
import org.mifos.visionppi.utils.PrefManager

class MenuPresenter : BasePresenter<MenuMVPView>() {

    private val gson: Gson = Gson()

    fun fetchUserDetails(activity: Activity, context: Context): User {
        val mPrefManager = PrefManager()
        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name), Context.MODE_PRIVATE)
        val userJsonString = sharedPref.getString(mPrefManager.userDetails, " ")

        return gson.fromJson(userJsonString, User::class.java)
    }
}
