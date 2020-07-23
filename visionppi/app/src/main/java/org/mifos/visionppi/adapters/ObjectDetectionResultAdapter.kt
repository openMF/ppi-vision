package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.object_detection_result.view.*
import org.mifos.visionppi.R

class ObjectDetectionResultAdapter(var result: List<List<String>>, var images: ArrayList<Bitmap?>, var context: Context) : RecyclerView.Adapter<ResultViewHolder>() {


    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.image?.setImageBitmap(images.get(position))
        var s: String = ""
        var ss = result[position]
        ss.forEach { s+= it + "\n" }
        holder.res1?.text = s
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(LayoutInflater.from(context).inflate(R.layout.object_detection_result, parent, false))
    }


    override fun getItemCount() : Int{
        return result.size
    }
}

class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView? = view.imageObject
    val res1: TextView? = view.item1
}




