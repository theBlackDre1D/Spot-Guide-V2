package com.g3.spot_guide.screens.todaySpot

import android.content.Context
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.TodaySpotBottomSheetFragmentBinding
import com.g3.spot_guide.extensions.onClick


class TodaySpotBottomSheetFragment : BaseBottomSheet<TodaySpotBottomSheetFragmentBinding, TodaySpotBottomSheetFragmentHandler>() {

    private val arguments: TodaySpotBottomSheetFragmentArgs by navArgs()

    override fun setBinding(layoutInflater: LayoutInflater) = TodaySpotBottomSheetFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: TodaySpotBottomSheetFragmentBinding, context: Context) {
        binding.timeTV.text = arguments.args.time
        binding.spotNameTV.text = arguments.args.spotName

        binding.spotNameTV.onClick {
            handler.openSpotDetailFromTodaySpotBottomSheet(arguments.args.spotId)
        }
    }
}

interface TodaySpotBottomSheetFragmentHandler : BaseFragmentHandler {
    fun openSpotDetailFromTodaySpotBottomSheet(spotId: String)
}