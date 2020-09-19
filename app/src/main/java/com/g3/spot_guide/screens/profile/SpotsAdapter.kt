package com.g3.spot_guide.screens.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.SpotItemBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.Spot

class SpotsAdapter(
    private val handler: SpotsAdapterHandler
) : BaseRecyclerViewAdapter<SpotsAdapter.SpotsAdapterViewHolder, SpotsAdapter.SpotsAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): SpotsAdapterViewHolder {
        return SpotsAdapterViewHolder(SpotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: SpotsAdapterViewHolder, adapterItem: SpotsAdapterItem, position: Int) {
        holder.binding.spotNameTV.text = adapterItem.spot.name

        holder.binding.root.onClick {
            handler.onSpotClick(adapterItem.spot)
        }
    }

    inner class SpotsAdapterViewHolder(val binding: SpotItemBinding) : BaseViewHolder(binding.root)
    data class SpotsAdapterItem(val spot: Spot) : BaseAdapterItem()

    interface SpotsAdapterHandler {
        fun onSpotClick(spot: Spot)
    }
}