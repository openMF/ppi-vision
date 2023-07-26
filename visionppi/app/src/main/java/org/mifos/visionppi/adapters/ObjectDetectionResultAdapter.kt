package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import org.mifos.visionppi.databinding.ObjectDetectionResultBinding

class ObjectDetectionResultAdapter(var result: List<List<String>>, var images: ArrayList<Bitmap?>, var context: Context) : RecyclerView.Adapter<ResultViewHolder>() {

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.image?.setImageBitmap(images[position])
        var s: String = ""
        val ss = result[position]
        ss.forEach { s += it + "\n" }
        holder.res1?.text = s
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ObjectDetectionResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return result.size
    }
}

class ResultViewHolder(val binding: ObjectDetectionResultBinding) : RecyclerView.ViewHolder(binding.root) {
    val image: ImageView? = binding.imageObject
    val res1: TextView? = binding.item1
}
