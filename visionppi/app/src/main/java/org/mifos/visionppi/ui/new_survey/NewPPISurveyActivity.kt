package org.mifos.visionppi.ui.new_survey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.new_survey_toolbar.*
import org.mifos.visionppi.R
import org.mifos.visionppi.objects.PPISurvey
import org.mifos.visionppi.objects.Response
import org.mifos.visionppi.ui.computer_vision.ComputerVisionActivity

class NewPPISurveyActivity : FragmentActivity(), NewSurveyMVPView {


    private lateinit var mPager: ViewPager
    var mNewSurveyPresenter : NewSurveyPresenter = NewSurveyPresenter()
    lateinit var mPPISurvey: PPISurvey

    companion object {
        var objectsDetected = "None"
        fun getObjects():String { return objectsDetected }
        fun setObjects(string: String){
            objectsDetected = string
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppi_view_pager)

        getSurveyQuestions()
        mPager = findViewById(R.id.ppi_view_pager)

        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter

    }

    override fun getSurveyQuestions() {
        mPPISurvey = mNewSurveyPresenter.getSurvey(1, applicationContext, this)
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = mPPISurvey.questionDatas.size

        override fun getItem(position: Int): Fragment = PPIQuestionFragment(mPPISurvey.questionDatas.get(position), applicationContext, { response: Response -> onResponseClicked(response) }, position, mPPISurvey.questionDatas.size)
    }


    override fun showToastMessage(string: String) {
        Toast.makeText(applicationContext ,string , Toast.LENGTH_LONG).show()
    }

    override fun renderSurvey(mPPISurvey: PPISurvey) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onResponseClicked( response: Response) {
        showToastMessage(response.text)
    }
}