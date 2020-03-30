package org.mifos.visionppi.adapters

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import org.mifos.visionppi.R
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.client_search_row.view.*
import org.mifos.visionppi.objects.Client

/**
 * Created by Apoorva M K on 01/07/19.
 */

class ClientSearchAdapter(var clientList: List<Client>, var context: Context, val listener: (Client) -> Unit) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.clientName!!.text = clientList.get(position).entityName
        holder.clientAccountNo!!.text = "#".plus(clientList.get(position).entityAccountNo)
        holder.setItem(clientList.get(position), listener)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.client_search_row, parent, false))
    }


    override fun getItemCount() : Int{
        return clientList.size
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val clientName : TextView? = view.client_name
    val clientAccountNo : TextView? = view.client_account_no
    val linearLayout : LinearLayout? = view.client_search_row

    fun setItem(item: Client, listener: (Client) -> Unit) {
        linearLayout!!.setOnClickListener { listener(item) }
    }
}




