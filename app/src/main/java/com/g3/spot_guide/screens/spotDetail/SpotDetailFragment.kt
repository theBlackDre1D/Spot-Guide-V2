package com.g3.spot_guide.screens.spotDetail

import GeoCoderUtils
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.g3.spot_guide.base.BaseBottomSheet
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import com.g3.spot_guide.screens.EmptyViewModel

class SpotDetailFragment : BaseBottomSheet<SpotDetailFragmentBinding, EmptyViewModel, SpotDetailFragmentHandler>() {

    private val arguments: SpotDetailFragmentArgs by navArgs()

    override val viewModel: EmptyViewModel by viewModels { EmptyViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {
        setupUI()
    }

    private fun setupUI() {
        val spot = arguments.args

        binding.spotNameTV.text = spot.name
        binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), spot.location)
        binding.descriptionContentTV.text = spot.description
    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler