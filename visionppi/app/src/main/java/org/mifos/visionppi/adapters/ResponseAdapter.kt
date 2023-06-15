package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mifos.visionppi.databinding.PpiResponseRowBinding
import org.mifos.visionppi.objects.Response

class ResponseAdapter(var responseList: List<Response>, var context: Context, private val responseClicked: (response: Response) -> Unit) : RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        val binding = PpiResponseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResponseViewHolder(binding)
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

    inner class ResponseViewHolder(val binding: PpiResponseRowBinding) : RecyclerView.ViewHolder(binding.root) {
        var responseButton: RadioButton? = binding.responseBtn
        var responseScore: TextView? = binding.responseScore

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
