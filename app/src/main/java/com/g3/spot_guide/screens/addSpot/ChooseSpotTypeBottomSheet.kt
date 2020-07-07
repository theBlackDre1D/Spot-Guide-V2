package com.g3.spot_guide.screens.addSpot

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.g3.spot_guide.base.BaseBottomSheet
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.ChooseSpotTypeDialogFragmentBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.screens.EmptyViewModel

class ChooseSpotTypeBottomSheet : BaseBottomSheet<ChooseSpotTypeDialogFragmentBinding, EmptyViewModel, ChooseSpotTypeBottomSheetHandler>(), ChooseSpotTypeAdapterHandler {

    private val adapter: ChooseSpotTypeAdapter by lazy { ChooseSpotTypeAdapter(this) }

    override val viewModel: EmptyViewModel by viewModels { EmptyViewModel.ViewModelInstanceFactory(this) }
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