package org.mifos.visionppi.ui.new_survey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.activity_new_survey.ppi_survey
import kotlinx.android.synthetic.main.new_survey_toolbar.computer_vision
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.QuestionAdapter
import org.mifos.visionppi.objects.PPISurvey
import org.mifos.visionppi.objects.Response
import org.mifos.visionppi.ui.computer_vision.ComputerVisionActivity

class NewSurveyActivity : AppCompatActivity(), NewSurveyMVPView {

    var mNewSurveyPresenter: NewSurveyPresenter = NewSurveyPresenter()
    lateinit var mPPISurvey: PPISurvey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_survey)

        getSurveyQuestions()

        computer_vision.setOnClickListener {
            val intent = Intent(applicationContext, ComputerVisionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun getSurveyQuestions() {
        mPPISurvey = mNewSurveyPresenter.getSurvey(1, applicationContext, this)
        renderSurvey(mPPISurvey)
    }

    override fun renderSurvey(mPPISurvey: PPISurvey) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(ppi_survey)
        ppi_survey.layoutManager = LinearLayoutManager(this)
        ppi_survey.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false))
        ppi_survey.adapter = QuestionAdapter(mPPISurvey.questionDatas, applicationContext) { response: Response -> onResponseClicked(response) }
    }

    override fun showToastMessage(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    fun onResponseClicked(response: Response) {
        showToastMessage(response.text)
    }
}
