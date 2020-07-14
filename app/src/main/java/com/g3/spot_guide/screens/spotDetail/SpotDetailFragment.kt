package com.g3.spot_guide.screens.spotDetail

import GeoCoderUtils
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.spot_guide.base.BaseBottomSheet
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SpotDetailFragment : BaseBottomSheet<SpotDetailFragmentBinding, SpotDetailFragmentHandler>(), SpotDetailPhotosAdapter.SpotDetailPhotosAdapterHandler {

    private val arguments: SpotDetailFragmentArgs by navArgs()

    private val photosAdapter: SpotDetailPhotosAdapter by lazy { SpotDetailPhotosAdapter(this) }

    private val spotDetailFragmentViewModel: SpotDetailFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {
        setupSpotData()
        setupImagesRV()
        downloadImages()
    }

    private fun setupSpotData() {
        val spot = arguments.args

        binding.spotNameTV.text = spot.name
        binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), spot.location)
        binding.descriptionContentTV.text = spot.description
        binding.spotTypeTV.text = spot.spotType
    }

    private fun setupImagesRV() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        binding.photosRV.adapter = photosAdapter
        binding.photosRV.layoutManager = layoutManager
        binding.photosRV.setHasFixedSize(true)
        binding.photosRV.addOnScrollListener(CenterScrollListener())
    }

    private fun downloadImages() {
        spotDetailFragmentViewModel.imagesUris.observe(this, Observer { imageUris ->
            val adapterItems = mutableListOf<SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem>()
            imageUris.forEach {
                adapterItems.add(SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem(it))
            }
            photosAdapter.injectData(adapterItems)
        })

        spotDetailFragmentViewModel.loadImages(arguments.args.images)
    }

    override fun onPhotoClick(position: Int) {
        handler.openImagesGallery(spotDetailFragmentViewModel.imagesUris.value ?: listOf(), position)
    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler {
    fun openImagesGallery(images: List<Uri>, position: Int)
}