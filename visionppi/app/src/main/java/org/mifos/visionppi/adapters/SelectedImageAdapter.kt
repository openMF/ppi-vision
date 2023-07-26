package org.mifos.visionppi.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.mifos.visionppi.databinding.ImageGridLayoutBinding

class SelectedImageAdapter(private val items: ArrayList<Bitmap?>, val context: Context, val removeImage: (position: Int) -> Unit) : RecyclerView.Adapter<SelectedImageAdapter.SelectedImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        val binding = ImageGridLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        holder.selectedImageImageview?.setImageBitmap(items[position])
        holder.setItem(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SelectedImageViewHolder(val binding: ImageGridLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val selectedImageImageview: ImageView? = binding.selectedImage
        private val removeButton: ImageButton? = binding.removeButton
        fun setItem(position: Int) {
            removeButton?.setOnClickListener { removeImage(position) }
        }
    }
}
