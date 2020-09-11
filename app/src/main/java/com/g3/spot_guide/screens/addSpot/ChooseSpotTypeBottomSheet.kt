package com.g3.spot_guide.screens.addSpot

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.ChooseSpotTypeBottomSheetBinding
import com.g3.spot_guide.enums.SpotType

class ChooseSpotTypeBottomSheet : BaseBottomSheet<ChooseSpotTypeBottomSheetBinding, ChooseSpotTypeBottomSheetHandler>(), ChooseSpotTypeAdapterHandler {

    private val adapter: ChooseSpotTypeAdapter by lazy { ChooseSpotTypeAdapter(this, ChooseSpotTypeAdapter.Mode.SINGLE_PICK) }

    override fun setBinding(layoutInflater: LayoutInflater): ChooseSpotTypeBottomSheetBinding = ChooseSpotTypeBottomSheetBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ChooseSpotTypeBottomSheetBinding, context: Context) {
        setupRV()
    }

    private fun setupRV() {
        binding.typesRV.adapter = adapter
        val items = mutableListOf<ChooseSpotTypeAdapter.ChooseSpotTypeAdapterItem>()
        SpotType.values().forEach { spotType ->
            items.add(ChooseSpotTypeAdapter.ChooseSpotTypeAdapterItem(spotType, false))
        }
        adapter.injectData(items)
    }

    override fun onSpotTypeCLick(spotType: SpotType) {
        handler.onTypePick(spotType)
        dismiss()
    }
}

interface ChooseSpotTypeBottomSheetHandler : BaseFragmentHandler {
    fun onTypePick(type: SpotType)
}