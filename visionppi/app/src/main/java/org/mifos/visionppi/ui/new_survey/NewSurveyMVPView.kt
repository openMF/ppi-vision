package org.mifos.visionppi.ui.new_survey

import org.mifos.visionppi.base.MVPView
import org.mifos.visionppi.objects.PPISurvey

interface NewSurveyMVPView : MVPView {

    fun getSurveyQuestions()

    fun renderSurvey(mPPISurvey: PPISurvey)
}