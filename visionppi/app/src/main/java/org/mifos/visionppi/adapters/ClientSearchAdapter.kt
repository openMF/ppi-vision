package org.mifos.visionppi.adapters

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.mifos.visionppi.R
import org.mifos.visionppi.databinding.ClientSearchRowBinding
import org.mifos.visionppi.objects.Client
import android.view.LayoutInflater

/**
 * Created by Apoorva M K on 01/07/19.
 */

class ClientSearchAdapter(var clientList: List<Client>, var context: Context, private val listener: (Client) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.clientName?.text = clientList[position].entityName
        holder.clientAccountNo?.text = "#".plus(clientList[position].entityAccountNo)
        holder.setItem(clientList[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ClientSearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return clientList.size
    }
}

class ViewHolder(val binding: ClientSearchRowBinding) : RecyclerView.ViewHolder(binding.root) {
    val clientName: TextView? = binding.clientName
    val clientAccountNo: TextView? =  binding.clientAccountNo
    private val linearLayout: LinearLayout? = binding.clientSearchRow

    fun setItem(item: Client, listener: (Client) -> Unit) {
        linearLayout?.setOnClickListener { listener(item) }
    }
}
