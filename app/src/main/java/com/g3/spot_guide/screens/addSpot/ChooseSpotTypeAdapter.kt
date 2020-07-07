package com.g3.spot_guide.screens.addSpot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.g3.spot_guide.databinding.SpotTypeItemBinding
import com.g3.spot_guide.enums.SpotType

class ChooseSpotTypeAdapter(
    private val listener: ChooseSpotTypeAdapterHandler,
    private val allowMultipleChoices: Boolean
) : RecyclerView.Adapter<ChooseSpotTypeAdapter.SpotTypeViewHolder>() {

    private val adapterItems = mutableListOf<SpotTypeViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotTypeViewHolder {
        return SpotTypeViewHolder(SpotTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = adapterItems.size

    override fun onBindViewHolder(holder: SpotTypeViewHolder, position: Int) {

    }

    inner class SpotTypeViewHolder(val binding: SpotTypeItemBinding) : RecyclerView.ViewHolder(binding.root)
}

interface ChooseSpotTypeAdapterHandler {
    fun onSpotTypeCLick(spotType: SpotType)
}