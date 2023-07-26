package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.mifos.visionppi.databinding.PpiQuestionLayoutBinding
import org.mifos.visionppi.objects.Question
import org.mifos.visionppi.objects.Response

class QuestionAdapter(var questionList: List<Question>, var context: Context, private val responseClick: (response: Response) -> Unit) : RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.question?.text = questionList[position].text
        holder.responseRecyclerView?.layoutManager = LinearLayoutManager(holder.responseRecyclerView?.context)
        holder.responseRecyclerView?.adapter = ResponseAdapter(questionList[position].responseDatas, context, responseClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = PpiQuestionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
}

class QuestionViewHolder(val binding: PpiQuestionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    val question: TextView? = binding.question
    val responseRecyclerView: RecyclerView? = binding.responses
}
