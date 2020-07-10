package com.g3.spot_guide.screens.addSpot

import android.content.Context
import android.view.LayoutInflater
import com.g3.spot_guide.base.BaseBottomSheet
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.ChooseSpotTypeDialogFragmentBinding
import com.g3.spot_guide.enums.SpotType

class ChooseSpotTypeBottomSheet : BaseBottomSheet<ChooseSpotTypeDialogFragmentBinding, ChooseSpotTypeBottomSheetHandler>(), ChooseSpotTypeAdapterHandler {

    private val adapter: ChooseSpotTypeAdapter by lazy { ChooseSpotTypeAdapter(this) }

    override fun setBinding(layoutInflater: LayoutInflater): ChooseSpotTypeDialogFragmentBinding = ChooseSpotTypeDialogFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ChooseSpotTypeDialogFragmentBinding, context: Context) {
        setupRV()
    }

    private fun setupRV() {
        binding.typesRV.adapter = adapter
        adapter.injectData(SpotType.values().toMutableList())
    }

    override fun onSpotTypeCLick(spotType: SpotType) {
        handler.onTypePick(spotType)
        dismiss()
    }
}

interface ChooseSpotTypeBottomSheetHandler : BaseFragmentHandler {
    fun onTypePick(type: SpotType)
}