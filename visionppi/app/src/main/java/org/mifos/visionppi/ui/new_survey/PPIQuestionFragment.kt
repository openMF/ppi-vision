package org.mifos.visionppi.ui.new_survey

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_cv_result_layout.*
import kotlinx.android.synthetic.main.new_survey_toolbar.*
import kotlinx.android.synthetic.main.new_survey_toolbar.view.*
import kotlinx.android.synthetic.main.new_survey_toolbar.view.computer_vision
import kotlinx.android.synthetic.main.ppi_question_layout.view.*
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ResponseAdapter
import org.mifos.visionppi.objects.Question
import org.mifos.visionppi.objects.Response
import org.mifos.visionppi.ui.computer_vision.ComputerVisionActivity


class PPIQuestionFragment( var questionData: Question, val mContext: Context, val responseClick :(response : Response) -> Unit, val position: Int, val size:Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.ppi_question_layout, container, false)
        view.question.text = questionData.text
        view.responses.layoutManager = LinearLayoutManager(context)
        view.responses.adapter = ResponseAdapter(questionData.responseDatas, mContext, responseClick)
        view.question_number.text = (position+1).toString().plus(" /").plus(size.toString())

        view.computer_vision.setOnClickListener {
                val intent = Intent(mContext, ComputerVisionActivity::class.java)
                startActivity(intent)
        }
        return view
    }

}