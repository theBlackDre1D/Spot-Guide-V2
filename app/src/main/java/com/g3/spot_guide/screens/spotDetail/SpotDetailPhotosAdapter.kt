package com.g3.spot_guide.screens.spotDetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.SpotPhotoItemBinding
import com.g3.spot_guide.extensions.loadImageFromUri
import com.g3.spot_guide.extensions.onClick

class SpotDetailPhotosAdapter(
    private val handler: SpotDetailPhotosAdapterHandler
) : BaseRecyclerViewAdapter<SpotDetailPhotosAdapter.SpotDetailPhotosAdapterViewHolder, SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): SpotDetailPhotosAdapterViewHolder {
        return SpotDetailPhotosAdapterViewHolder(SpotPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: SpotDetailPhotosAdapterViewHolder, adapterItem: SpotDetailPhotosAdapterItem, position: Int) {
        holder.binding.photoIV.loadImageFromUri(adapterItem.photoUri)
        holder.binding.deletePhotoIV.isVisible = false

        holder.binding.photoIV.onClick {
            handler.onPhotoClick(position)
        }
    }

    class SpotDetailPhotosAdapterViewHolder(val binding: SpotPhotoItemBinding) : BaseViewHolder(binding.root)
    data class SpotDetailPhotosAdapterItem(val photoUri: Uri) : BaseAdapterItem()

    interface SpotDetailPhotosAdapterHandler {
        fun onPhotoClick(position: Int)
    }
}