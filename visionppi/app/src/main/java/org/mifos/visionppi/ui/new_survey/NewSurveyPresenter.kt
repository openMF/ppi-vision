package org.mifos.visionppi.ui.new_survey

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import khttp.get
import khttp.structures.authorization.BasicAuthorization
import org.json.JSONObject
import org.mifos.visionppi.R
import org.mifos.visionppi.api.APIEndPoint
import org.mifos.visionppi.base.BasePresenter
import org.mifos.visionppi.objects.PPISurvey
import org.mifos.visionppi.utils.PrefManager

class NewSurveyPresenter : BasePresenter<NewSurveyMVPView>() {

    lateinit var mPPISurvey : PPISurvey

    fun getSurvey(surveyId : Int, context: Context, activity: Activity) : PPISurvey{
        val url = context.getString(R.string.demoURL).plus(APIEndPoint.SURVEY)
                                                            .plus("/"+surveyId.toString())
        val tenantId  = context.getString(R.string.tenantId)
        val contentType  = context.getString(R.string.contentType)

        val header = mapOf("Fineract-Platform-TenantId" to tenantId,
                                            "Content-Type" to contentType)
        val sharedPref = activity.getSharedPreferences(context.getString(R.string.pref_file_name),Context.MODE_PRIVATE)
        val mPrefManager = PrefManager()
        val username = sharedPref.getString(mPrefManager.USERNAME, " ")
        val password = sharedPref.getString(mPrefManager.PASSWORD, " ")


        try {

            var response = get(url = url, headers = header, auth= BasicAuthorization(username, password))

            if (response.statusCode == 200) {

                val obj:JSONObject = response.jsonObject
                var gson: Gson = Gson()
                mPPISurvey  = gson.fromJson(obj.toString(), PPISurvey::class.java)
            }



        }catch (e:Exception){
            Log.i(context.getString(R.string.exception),e.toString())
        }

        return mPPISurvey
    }

}