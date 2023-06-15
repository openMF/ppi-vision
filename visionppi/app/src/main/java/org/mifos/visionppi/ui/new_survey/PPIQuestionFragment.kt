package org.mifos.visionppi.ui.new_survey

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.mifos.visionppi.R
import org.mifos.visionppi.adapters.ResponseAdapter
import org.mifos.visionppi.databinding.NewSurveyToolbarBinding
import org.mifos.visionppi.databinding.PpiQuestionLayoutBinding
import org.mifos.visionppi.objects.Question
import org.mifos.visionppi.objects.Response
import org.mifos.visionppi.ui.computer_vision.ComputerVisionActivity

class PPIQuestionFragment(var questionData: Question, private val mContext: Context, private val responseClick: (response: Response) -> Unit) : Fragment() {
    private lateinit var ppiQuestionLayoutBinding: PpiQuestionLayoutBinding
    val list get() = ComputerVisionActivity.finalLabels

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        ppiQuestionLayoutBinding=PpiQuestionLayoutBinding.inflate(layoutInflater)
        ppiQuestionLayoutBinding.responses.adapter = ResponseAdapter(questionData.responseDatas, mContext, responseClick)
        val view = ppiQuestionLayoutBinding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        ppiQuestionLayoutBinding.question.text = questionData.text
        ppiQuestionLayoutBinding.responses.layoutManager = LinearLayoutManager(context)
        if (!list.isNullOrEmpty()) {
            var s: String = ""
            var c: Int = 0
            var thatch = false
            var vehicle = false
            var sc = false
            var fridge = false
            var washer = false
            var tf = false
            var tv = false
            var player = false
            var table = false
            var ln = false
            var bm = false
            var stove = false
            var iron = false
            list.forEach { it ->
                it.forEach {
                    if (it.contains("thatch", ignoreCase = true)) {
                        thatch = true
                    } else if (it.contains("lanterns", ignoreCase = true) || it.contains("lamp", ignoreCase = true) || it.contains("table lamp", ignoreCase = true)) {
                        ln = true
                    } else if (it.contains("table", ignoreCase = true) || it.contains("dining table", ignoreCase = true)) {
                        table = true
                    } else if (it.contains("vehicle", ignoreCase = true) || it.contains("bicycle", ignoreCase = true) || it.contains("motorcycle", ignoreCase = true) || it.contains("car", ignoreCase = true)) {
                        vehicle = true
                    } else if (it.contains("toilet facility", ignoreCase = true) || it.contains("sanitation facility", ignoreCase = true) || it.contains("toilet seat", ignoreCase = true)) {
                        tf = true
                    } else if (it.contains("mobile home", ignoreCase = true) || it.contains("patio", ignoreCase = true)) {
                        bm = true
                    } else if (it.contains("stove", ignoreCase = true) || it.contains("gas stove", ignoreCase = true) || it.contains("electric stove", ignoreCase = true)) {
                        stove = true
                    } else if (it.contains("iron", ignoreCase = true) || it.contains("electric iron", ignoreCase = true) || it.contains("waffle iron", ignoreCase = true)) {
                        iron = true
                    } else if (it.contains("studio couch", ignoreCase = true)) {
                        sc = true
                    } else if (it.contains("refrigerator", ignoreCase = true)) {
                        fridge = true
                    } else if (it.contains("washer", ignoreCase = true)) {
                        washer = true
                    } else if (it.contains("television", ignoreCase = true) || it.contains("entertainment center", ignoreCase = true) || it.contains("home theater", ignoreCase = true)) {
                        tv = true
                    } else if (it.contains("cassette player", ignoreCase = true) || it.contains("CD player", ignoreCase = true) || it.contains("tape player", ignoreCase = true) || it.contains("tape recorder", ignoreCase = true) || it.contains("cassette", ignoreCase = true)) {
                        player = true
                    } else if (it.contains("cellular telephone", ignoreCase = true) || it.contains("dial telephone", ignoreCase = true)) {
                        ++c
                    }
                }
            }
            if (questionData.text.contains("building materials", ignoreCase = true)) {
                s = if (bm) {
                    "The outer walls are made up of predominantly light materials"
                } else {
                    "The outer walls are made of predominantly strong or strong materials"
                }
            } else if (questionData.text.contains("roof material", ignoreCase = true)) {
                s = if (thatch) {
                    "The roofs covering is made up of light material(straw,palm leaves)"
                } else {
                    "The roof covering is made up of strong material(cement, clay tile)"
                }
            } else if (questionData.text.contains("sala sets", ignoreCase = true)) {
                s = if (sc) {
                    "There is atleast 1 salsa set present"
                } else {
                    "There is no salsa set"
                }
            } else if (questionData.text.contains("table", ignoreCase = true)) {
                s = if (table) {
                    "There is atleast 1 table present"
                } else {
                    "There is no table"
                }
            } else if (questionData.text.contains("iron", ignoreCase = true)) {
                s = if (iron) {
                    "There is atleast 1 iron present"
                } else {
                    "There is no iron"
                }
            } else if (questionData.text.contains("stove", ignoreCase = true)) {
                s = if (stove) {
                    "There is atleast 1 stove present"
                } else {
                    "There is no stove"
                }
            } else if (questionData.text.contains("lanterns", ignoreCase = true)) {
                s = if (ln) {
                    "There is atleast 1 lanterns present"
                } else {
                    "There is no lanterns"
                }
            } else if (questionData.text.contains("toilet facility", ignoreCase = true)) {
                if (tf) {
                    s = "There is portable toilet facility present"
                } else if (tf) {
                    s = "There is japanese toilet facility present"
                } else if (tf) {
                    s = "There is western toilet facility present"
                }
            } else if (questionData.text.contains("television", ignoreCase = true)) {
                s = if (tv) {
                    "There is atleast 1 television present"
                } else {
                    "There is no television"
                }
            } else if (questionData.text.contains("cassette", ignoreCase = true)) {
                s = if (player) {
                    "There is atleast 1 cassette present"
                } else {
                    "There is no cassette"
                }
            } else if (questionData.text.contains("vehicle", ignoreCase = true)) {
                s = if (vehicle) {
                    "There is atleast 1 vechicle(motorcycle/bicycle) present"
                } else {
                    "There is no vehicle present"
                }
            } else if (questionData.text.contains("washing machine", ignoreCase = true)) {
                s = if (fridge && !washer) {
                    "Only a refrigerator is present"
                } else if (washer && !fridge) {
                    "Only a washing machine is present"
                } else if (washer && fridge) {
                    "Both are present"
                } else {
                    "Neither are present"
                }
            } else if (questionData.text.contains("telephones", ignoreCase = true)) {
                s = "There are $c telephones detected"
            }

            ppiQuestionLayoutBinding.res.text = s

            ppiQuestionLayoutBinding.res.visibility = View.VISIBLE
        }
    }
}
