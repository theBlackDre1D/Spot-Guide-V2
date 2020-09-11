package com.g3.spot_guide.screens.map

import android.content.Context
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.FilterSpotBottomSheetBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.screens.addSpot.ChooseSpotTypeAdapter
import com.g3.spot_guide.screens.addSpot.ChooseSpotTypeAdapterHandler
import java.io.Serializable

data class FilterSpotsBottomSheetArguments(
    val pickedSpotTypes: List<SpotType>
) : Serializable

class FilterSpotsBottomSheet : BaseBottomSheet<FilterSpotBottomSheetBinding, FilterSpotsBottomSheetHandler>(), ChooseSpotTypeAdapterHandler {

    private val arguments: FilterSpotsBottomSheetArgs by navArgs()

    private val adapter: ChooseSpotTypeAdapter by lazy { ChooseSpotTypeAdapter(this, ChooseSpotTypeAdapter.Mode.MULTIPLE_PICKS) }

    override fun setBinding(layoutInflater: LayoutInflater): FilterSpotBottomSheetBinding = FilterSpotBottomSheetBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: FilterSpotBottomSheetBinding, context: Context) {
        setupRV()
    }

    private fun setupRV() {
        binding.typesRV.adapter = adapter
        val items = mutableListOf<ChooseSpotTypeAdapter.ChooseSpotTypeAdapterItem>()
        SpotType.values().forEach { spotType ->
            items.add(ChooseSpotTypeAdapter.ChooseSpotTypeAdapterItem(spotType, arguments.arguments.pickedSpotTypes.contains(spotType)))
        }
        adapter.injectData(items)
    }

    override fun onSpotTypeCLick(spotType: SpotType) {
        handler.onSpotTypeCLick(spotType)
    }
}

interface FilterSpotsBottomSheetHandler : BaseFragmentHandler {
    fun onSpotTypeCLick(spotType: SpotType)
}