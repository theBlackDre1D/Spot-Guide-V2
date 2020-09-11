package com.g3.spot_guide.screens.addSpot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.SpotTypeItemBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.onClick

class ChooseSpotTypeAdapter(
    private val handler: ChooseSpotTypeAdapterHandler,
    private val mode: Mode
) : BaseRecyclerViewAdapter<ChooseSpotTypeAdapter.ChooseSpotTypeAdapterViewHolder, ChooseSpotTypeAdapter.ChooseSpotTypeAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): ChooseSpotTypeAdapterViewHolder {
        return ChooseSpotTypeAdapterViewHolder(SpotTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: ChooseSpotTypeAdapterViewHolder, adapterItem: ChooseSpotTypeAdapterItem, position: Int) {
        holder.binding.typeTV.text = adapterItem.spotType.spotName

        holder.binding.root.onClick {
            handler.onSpotTypeCLick(adapterItem.spotType)
            holder.binding.typeCHB.isChecked = !holder.binding.typeCHB.isChecked
        }

        holder.binding.typeCHB.isVisible = mode == Mode.MULTIPLE_PICKS
        holder.binding.typeCHB.isChecked = adapterItem.isPicked
    }

    class ChooseSpotTypeAdapterViewHolder(val binding: SpotTypeItemBinding) : BaseViewHolder(binding.root)
    data class ChooseSpotTypeAdapterItem(val spotType: SpotType, val isPicked: Boolean) : BaseAdapterItem()

    enum class Mode {
        SINGLE_PICK, MULTIPLE_PICKS
    }
}

interface ChooseSpotTypeAdapterHandler {
    fun onSpotTypeCLick(spotType: SpotType)
}