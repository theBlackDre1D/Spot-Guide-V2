package com.g3.spot_guide.screens.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.g3.spot_guide.databinding.GalleryImageItemBinding
import com.g3.spot_guide.extensions.loadImageFromUri
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel

class GalleryAdapter(
    private val handler: GalleryAdapterHandler
) : RecyclerView.Adapter<GalleryAdapter.GalleryAdapterViewHolder>() {

    private val adapterItems = mutableListOf<GalleryAdapterItem>()

    fun injectData(newItems: MutableList<GalleryAdapterItem>) {
        adapterItems.clear()
        adapterItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryAdapterViewHolder {
        return GalleryAdapterViewHolder(GalleryImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: GalleryAdapterViewHolder, position: Int) {
        val adapterItem = adapterItems[position]
        holder.binding.photoIV.loadImageFromUri(adapterItem.imageModel.uri)
        holder.binding.blurV.isVisible = adapterItem.isSelected

        holder.binding.root.onClick {
            holder.binding.blurV.isVisible = !holder.binding.blurV.isVisible
            handler.onImageClick(adapterItem.imageModel)
        }
    }

    inner class GalleryAdapterViewHolder(val binding: GalleryImageItemBinding) : RecyclerView.ViewHolder(binding.root)
    data class GalleryAdapterItem(val imageModel: ImageModel, val isSelected: Boolean)

    interface GalleryAdapterHandler {
        fun onImageClick(imageModel: ImageModel)
    }

}