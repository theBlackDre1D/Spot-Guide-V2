package com.g3.spot_guide.screens.addSpot

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g3.spot_guide.base.BaseAdapterItem
import com.g3.spot_guide.base.BaseRecyclerViewAdapter
import com.g3.spot_guide.base.BaseViewHolder
import com.g3.spot_guide.databinding.SpotPhotoItemBinding
import com.g3.spot_guide.extensions.loadImageFromUri
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel


class PhotosAdapter(
    private val handler: PhotosAdapterHandler
) : BaseRecyclerViewAdapter<PhotosAdapter.PhotosAdapterViewHolder, PhotosAdapter.PhotosAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        return PhotosAdapterViewHolder(SpotPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: PhotosAdapterViewHolder, adapterItem: PhotosAdapterItem, position: Int) {
        holder.binding.photoIV.loadImageFromUri(adapterItem.imageModel.uri)

        holder.binding.deletePhotoIV.onClick {
            handler.onDeletePhoto(adapterItem.imageModel)
        }
    }

    inner class PhotosAdapterViewHolder(val binding: SpotPhotoItemBinding) : BaseViewHolder(binding.root)
    data class PhotosAdapterItem(val imageModel: ImageModel) : BaseAdapterItem()

    interface PhotosAdapterHandler {
        fun onDeletePhoto(imageModel: ImageModel)
    }
}