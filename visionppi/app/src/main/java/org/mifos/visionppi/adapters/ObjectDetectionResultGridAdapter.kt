package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.mifos.visionppi.R

class ObjectDetectionGridAdapter(
        private val results: List<List<String>>,
        private val images: List<Bitmap?>,
        private val context: Context
) : BaseAdapter() {

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(position: Int): Any {
        return results[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val inflater = LayoutInflater.from(context)

        if (convertView == null) {
            view = inflater.inflate(R.layout.child_grid, parent, false)
        } else {
            view = convertView
        }

        val imageView: ImageView = view.findViewById(R.id.imageObject)
        val res1: TextView = view.findViewById(R.id.item1)

        imageView.setImageBitmap(images[position])
        val resultText = results[position].joinToString("\n")
        res1.text = resultText

        return view
    }
}

