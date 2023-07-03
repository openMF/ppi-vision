package org.mifos.visionppi.ui.new_survey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.QuestionAdapter
import org.mifos.visionppi.databinding.ActivityComputerVisionBinding
import org.mifos.visionppi.databinding.ActivityNewSurveyBinding
import org.mifos.visionppi.databinding.NewSurveyToolbarBinding
import org.mifos.visionppi.objects.PPISurvey
import org.mifos.visionppi.objects.Response
import org.mifos.visionppi.ui.computer_vision.ComputerVisionActivity

class NewSurveyActivity : AppCompatActivity(), NewSurveyMVPView {
    private lateinit var newSurveyToolbarBinding: NewSurveyToolbarBinding
    private lateinit var activityNewSurveyBinding: ActivityNewSurveyBinding
    private var mNewSurveyPresenter: NewSurveyPresenter = NewSurveyPresenter()
    lateinit var mPPISurvey: PPISurvey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_survey)
        newSurveyToolbarBinding=NewSurveyToolbarBinding.inflate(layoutInflater)
        activityNewSurveyBinding=ActivityNewSurveyBinding.inflate(layoutInflater)
        getSurveyQuestions()

        newSurveyToolbarBinding.computerVision.setOnClickListener {
            val intent = Intent(applicationContext, ComputerVisionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getSurveyQuestions() {
        mPPISurvey = mNewSurveyPresenter.getSurvey(5, applicationContext, this)
        renderSurvey(mPPISurvey)
    }

    override fun renderSurvey(mPPISurvey: PPISurvey) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(activityNewSurveyBinding.ppiSurvey)
        activityNewSurveyBinding.ppiSurvey.layoutManager = LinearLayoutManager(this)
        activityNewSurveyBinding.ppiSurvey.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        activityNewSurveyBinding.ppiSurvey.adapter = QuestionAdapter(mPPISurvey.questionDatas, applicationContext) { response: Response -> onResponseClicked(response) }
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun onResponseClicked(response: Response) {
        showToastMessage(response.text)
    }
}
