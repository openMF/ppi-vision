package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_grid_layout.view.*
import org.mifos.visionppi.R


class SelectedImageAdapter(val items : ArrayList<Bitmap?>, val context: Context) : RecyclerView.Adapter<SelectedImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(LayoutInflater.from(context).inflate(R.layout.image_grid_layout, parent, false))
    }


    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        holder.selected_image_imageview.setImageBitmap(items.get(position))
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class SelectedImageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val selected_image_imageview = view.selected_image
}

