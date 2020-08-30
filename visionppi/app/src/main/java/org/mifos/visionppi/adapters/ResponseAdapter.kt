package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ppi_response_row.view.response_btn
import kotlinx.android.synthetic.main.ppi_response_row.view.response_score
import org.mifos.visionppi.objects.Response

class ResponseAdapter(var responseList: List<Response>, var context: Context, private val responseClicked: (response: Response) -> Unit) : RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        return ResponseViewHolder(LayoutInflater.from(context).inflate(org.mifos.visionppi.R.layout.ppi_response_row, parent, false))
    }

    override fun getItemCount(): Int {
        return responseList.size
    }

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        holder.responseButton?.text = responseList[position].text
        holder.responseScore?.text = responseList[position].value.toString() + " Pts"
        holder.responseButton?.isChecked = responseList[position].isChecked
        holder.setItem(position, responseClicked)
    }

    inner class ResponseViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var responseButton: RadioButton? = view.response_btn
        var responseScore: TextView? = view.response_score

        fun setItem(position: Int, responseClicked: (response: Response) -> Unit) {
            responseButton?.setOnClickListener {
                for (responseListPosition in 0..responseList.lastIndex) {
                    responseList[responseListPosition].isChecked = false
                }
                responseButton?.isChecked = true
                responseList[position].isChecked = true
                responseClicked(responseList[position])
                notifyDataSetChanged()
            }
        }
    }
}
