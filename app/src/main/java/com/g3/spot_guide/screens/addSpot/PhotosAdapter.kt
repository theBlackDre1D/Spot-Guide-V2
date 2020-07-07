package com.g3.spot_guide.screens.addSpot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g3.spot_guide.databinding.SpotPhotoItemBinding
import com.g3.spot_guide.extensions.loadImageFromUri
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel


class PhotosAdapter(
    private val handler: PhotosAdapterHandler
) : RecyclerView.Adapter<PhotosAdapter.PhotosAdapterViewHolder>() {

    private val adapterItems = mutableListOf<PhotosAdapterItem>()

    fun injectItems(newItems: MutableList<PhotosAdapterItem>) {
        adapterItems.clear()
        adapterItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        return PhotosAdapterViewHolder(SpotPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: PhotosAdapterViewHolder, position: Int) {
        val adapterItem = adapterItems[position]
        holder.binding.photoIV.loadImageFromUri(adapterItem.imageModel.uri)

        holder.binding.root.onClick {
            handler.onPhotoClick(adapterItem.imageModel)
        }

        holder.binding.deletePhotoIV.onClick {
            handler.onDeletePhoto(adapterItem.imageModel)
        }
    }

    inner class PhotosAdapterViewHolder(val binding: SpotPhotoItemBinding) : RecyclerView.ViewHolder(binding.root)
    data class PhotosAdapterItem(val imageModel: ImageModel)

    interface PhotosAdapterHandler {
        fun onPhotoClick(imageModel: ImageModel)
        fun onDeletePhoto(imageModel: ImageModel)
    }
}