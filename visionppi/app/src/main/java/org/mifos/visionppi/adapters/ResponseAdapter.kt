package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ppi_response_row.view.*
import org.mifos.visionppi.objects.Response


class ResponseAdapter(var responseList: List<Response>,var context: Context, val responseClicked :(response: Response)->Unit) : RecyclerView.Adapter<ResponseAdapter.ResponseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResponseViewHolder {
        return ResponseViewHolder(LayoutInflater.from(context).inflate(org.mifos.visionppi.R.layout.ppi_response_row, parent, false))
    }

    override fun getItemCount(): Int {
        return responseList.size
    }

    override fun onBindViewHolder(holder: ResponseViewHolder, position: Int) {
        holder.responseButton.setText(responseList.get(position).text)
        holder.responseScore.text = responseList.get(position).value.toString()
        holder.responseButton.setOnClickListener {  }
        holder.setItem(responseList.get(position), responseClicked)

    }

    class ResponseViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var responseButton = view.response_btn
        var responseScore = view.response_score

        fun setItem(response: Response, responseClicked: (response: Response) -> Unit)
        {
            responseButton.setOnClickListener {  responseClicked(response) }
        }
    }

}


