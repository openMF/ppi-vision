package org.mifos.visionppi.ui.new_survey

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import khttp.get
import org.json.JSONObject
import org.mifos.visionppi.R
import org.mifos.visionppi.api.ApiEndPoints
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.objects.PPISurvey
import org.mifos.visionppi.ui.user_profile.UserProfilePresenter
import org.mifos.visionppi.utils.AuthKey

class NewSurveyPresenter : BasePresenter<NewSurveyMVPView>() {

    lateinit var mPPISurvey: PPISurvey
    var mUserProfilePresenter: UserProfilePresenter = UserProfilePresenter()

    fun getSurvey(surveyId: Int, context: Context, activity: Activity): PPISurvey {
        val url = context.getString(R.string.demoURL).plus(ApiEndPoints.SURVEY)
                .plus("/$surveyId")
        val tenantId = context.getString(R.string.tenantId)
        val contentType = context.getString(R.string.contentType)

        val header = mapOf("Fineract-Platform-TenantId" to tenantId,
                "Content-Type" to contentType)

        val user = mUserProfilePresenter.fetchUserDetails(activity, context)

        val response = get(url = url, headers = header, auth = AuthKey((user.base64EncodedAuthenticationKey).toString()))

        val obj: JSONObject = response.jsonObject
        val gson: Gson = Gson()
        mPPISurvey = gson.fromJson(obj.toString(), PPISurvey::class.java)

        return mPPISurvey
    }
}
