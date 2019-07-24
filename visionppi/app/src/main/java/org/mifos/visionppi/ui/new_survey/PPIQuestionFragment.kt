package org.mifos.visionppi.ui.new_survey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.ppi_question_layout.view.*
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ResponseAdapter
import org.mifos.visionppi.objects.Question
import org.mifos.visionppi.objects.Response


class PPIQuestionFragment( var questionData: Question, val mContext: Context, val responseClick :(response : Response) -> Unit) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.ppi_question_layout, container, false)
        view.question.text = questionData.text
        view.responses.layoutManager = LinearLayoutManager(context)
        view.responses.adapter = ResponseAdapter(questionData.responseDatas, mContext, responseClick)
        return view
    }

}