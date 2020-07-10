package com.g3.spot_guide.screens.spotDetail

import GeoCoderUtils
import android.content.Context
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.spot_guide.base.BaseBottomSheet
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.screens.addSpot.PhotosAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class SpotDetailFragment : BaseBottomSheet<SpotDetailFragmentBinding, SpotDetailFragmentHandler>(), PhotosAdapter.PhotosAdapterHandler {

    private val arguments: SpotDetailFragmentArgs by navArgs()

    private val photosAdapter: PhotosAdapter by lazy { PhotosAdapter(this) }

    private val spotDetailFragmentViewModel: SpotDetailFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {
        setupSpotData()
        setupImagesRV()
    }

    private fun setupSpotData() {
        val spot = arguments.args

        binding.spotNameTV.text = spot.name
        binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), spot.location)
        binding.descriptionContentTV.text = spot.description
    }

    private fun setupImagesRV() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        binding.photosRV.adapter = photosAdapter
        binding.photosRV.layoutManager = layoutManager
        binding.photosRV.setHasFixedSize(true)
        binding.photosRV.addOnScrollListener(CenterScrollListener())
    }

    override fun onPhotoClick(imageModel: ImageModel) {
        // TODO
    }

    override fun onDeletePhoto(imageModel: ImageModel) {
        // nothing
    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler