package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.image_grid_layout.view.remove_button
import kotlinx.android.synthetic.main.image_grid_layout.view.selected_image
import org.mifos.visionppi.R

class SelectedImageAdapter(private val items: ArrayList<Bitmap?>, val context: Context, val removeImage: (position: Int) -> Unit) : RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(LayoutInflater.from(context).inflate(R.layout.image_grid_layout, parent, false))
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        holder.selectedImageImageview?.setImageBitmap(items.get(position))
        holder.setItem(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SelectedImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val selectedImageImageview: ImageView? = view.selected_image
        private val removeButton: ImageButton? = view.remove_button
        fun setItem(position: Int) {
            removeButton?.setOnClickListener { removeImage(position) }
        }
    }
}
