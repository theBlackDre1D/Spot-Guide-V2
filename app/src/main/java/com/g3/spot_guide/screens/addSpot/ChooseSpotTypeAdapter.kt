package com.g3.spot_guide.screens.addSpot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g3.spot_guide.databinding.SpotTypeItemBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.onClick

class ChooseSpotTypeAdapter(
    private val handler: ChooseSpotTypeAdapterHandler
) : RecyclerView.Adapter<ChooseSpotTypeAdapter.ChooseSpotTypeAdapterViewHolder>() {

    private val adapterItems = mutableListOf<SpotType>()

    fun injectData(newItems: MutableList<SpotType>) {
        adapterItems.clear()
        adapterItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseSpotTypeAdapterViewHolder {
        return ChooseSpotTypeAdapterViewHolder(SpotTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: ChooseSpotTypeAdapterViewHolder, position: Int) {
        val adapterItem = adapterItems[position]
        holder.binding.typeTV.text = adapterItem.spotName

        holder.binding.root.onClick {
            handler.onSpotTypeCLick(adapterItem)
        }
    }

    inner class ChooseSpotTypeAdapterViewHolder(val binding: SpotTypeItemBinding) : RecyclerView.ViewHolder(binding.root)
}

interface ChooseSpotTypeAdapterHandler {
    fun onSpotTypeCLick(spotType: SpotType)
}