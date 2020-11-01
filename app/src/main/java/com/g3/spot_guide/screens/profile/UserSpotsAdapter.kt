package com.g3.spot_guide.screens.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.UserSpotItemBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.Spot

class UserSpotsAdapter(
    private val handler: SpotsAdapterHandler
) : BaseRecyclerViewAdapter<UserSpotsAdapter.UserSpotsAdapterViewHolder, UserSpotsAdapter.UserSpotsAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): UserSpotsAdapterViewHolder {
        return UserSpotsAdapterViewHolder(UserSpotItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: UserSpotsAdapterViewHolder, adapterItem: UserSpotsAdapterItem, position: Int) {
        holder.binding.spotNameTV.text = adapterItem.spot.name

        holder.binding.root.onClick {
            handler.onSpotClick(adapterItem.spot)
        }
    }

    inner class UserSpotsAdapterViewHolder(val binding: UserSpotItemBinding) : BaseViewHolder(binding.root)
    data class UserSpotsAdapterItem(val spot: Spot) : BaseAdapterItem()

    interface SpotsAdapterHandler {
        fun onSpotClick(spot: Spot)
    }
}