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


class PPIQuestionFragment(var questionData: Question, val mContext: Context, val list: MutableList<List<String>>, val responseClick: (response: Response) -> Unit) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.ppi_question_layout, container, false)
        view.question.text = questionData.text
        view.responses.layoutManager = LinearLayoutManager(context)
        if (!list.isNullOrEmpty()) {
            var s: String = ""
            var c: Int = 0
            var thatch = false
            var sc = false
            var fridge = false
            var washer = false
            var tv = false
            var player = false
            var mh = false
            list.forEach {
                it.forEach {
                    if (it.contains("thatch", ignoreCase = true)) {
                        thatch = true
                    } else if (it.contains("mobile home", ignoreCase = true) || it.contains("patio", ignoreCase = true)) {
                        mh = true
                    } else if (it.contains("studio couch", ignoreCase = true)) {
                        sc = true
                    } else if (it.contains("refrigerator", ignoreCase = true)) {
                        fridge = true
                    } else if (it.contains("washer", ignoreCase = true)) {
                        washer = true
                    } else if (it.contains("television", ignoreCase = true) || it.contains("entertainment center", ignoreCase = true) || it.contains("home theater", ignoreCase = true)) {
                       tv = true
                    } else if (it.contains("cassette player", ignoreCase = true) || it.contains("CD player", ignoreCase = true) || it.contains("tape player", ignoreCase = true)) {
                        player = true
                    } else if (it.contains("cellular telephone", ignoreCase = true) || it.contains("dial telephone", ignoreCase = true)) {
                        ++c;
                    }
                }
            }

            if (questionData.text.contains("construction materials", ignoreCase = true)) {
                if (thatch && !mh) {
                    s = "The outer walls are made up of predominantly light materials"
                } else {
                    s = "The outer walls are made of predominantly strong or strong materials"
                }
            } else if (questionData.text.contains("sala sets", ignoreCase = true)) {
                if (sc) {
                    s = "There is atleast 1 salsa set present"
                } else {
                    s = "There is no salsa set"
                }
            } else if (questionData.text.contains("washing machine", ignoreCase = true)) {
                if (fridge && !washer) {
                    s = "Only a refrigerator is present"
                } else if (washer && !fridge) {
                    s = "Only a washing machine is present"
                } else if (washer && fridge) {
                    s = "Both are present"
                } else {
                    s = "Neither are present"
                }
            } else if (questionData.text.contains("television", ignoreCase = true)) {
                if (tv && !player) {
                    s = "Only television is present"
                } else if (player && !tv) {
                    s = "A VTR/VHS/VCD/DVD Player is present"
                } else if (!tv && !player) {
                    s = "Neither is pres"
                }
            } else if (questionData.text.contains("telephones", ignoreCase = true)) {
                s = "There are $c telephones detected"
            }

            view.res.text = s
            view.res.visibility = View.VISIBLE
        }
        view.responses.adapter = ResponseAdapter(questionData.responseDatas, mContext, responseClick)
        return view
    }

}