package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ppi_question_layout.view.question
import kotlinx.android.synthetic.main.ppi_question_layout.view.responses
import org.mifos.visionppi.R
import org.mifos.visionppi.objects.Question
import org.mifos.visionppi.objects.Response

class QuestionAdapter(var questionList: List<Question>, var context: Context, val responseClick: (response: Response) -> Unit) : RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.question?.text = questionList.get(position).text
        holder.responseRecyclerView?.layoutManager = LinearLayoutManager(holder.responseRecyclerView?.context)
        holder.responseRecyclerView?.adapter = ResponseAdapter(questionList.get(position).responseDatas, context, responseClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(LayoutInflater.from(context).inflate(R.layout.ppi_question_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}

class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val question: TextView? = view.question
    val responseRecyclerView: RecyclerView? = view.responses
}
