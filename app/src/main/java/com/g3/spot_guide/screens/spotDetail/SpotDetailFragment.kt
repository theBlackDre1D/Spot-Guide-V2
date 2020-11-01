package com.g3.spot_guide.screens.spotDetail

import GeoCoderUtils
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.base.either.Either
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.Spot
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

data class SpotDetailFragmentArguments(
    val spot: Spot?,
    val spotId: String?
) : Serializable

class SpotDetailFragment : BaseBottomSheet<SpotDetailFragmentBinding, SpotDetailFragmentHandler>(), SpotDetailPhotosAdapter.SpotDetailPhotosAdapterHandler {

    private val arguments: SpotDetailFragmentArgs by navArgs()

    private val photosAdapter: SpotDetailPhotosAdapter by lazy { SpotDetailPhotosAdapter(this) }

    private val spotDetailFragmentViewModel: SpotDetailFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {
        setupSpotObserver()
        setupSpotData()
        setupImagesRV()
        downloadImages()
        setupButtons()
    }

    private fun setupSpotData() {
        val spot = arguments.spotArguments.spot

        if (spot != null) {
            spotDetailFragmentViewModel.spot.postValue(Either.Success(spot))
        } else {
            arguments.spotArguments.spotId?.let { spotId ->
                spotDetailFragmentViewModel.getSpot(spotId)
            }
        }
    }

    private fun setupSpotObserver() {
        spotDetailFragmentViewModel.spot.observe(this, { spotEither ->
            val spotValue = spotEither.getValueOrNull()
            if (spotValue != null) {
                binding.spotNameTV.text = spotValue.name
                binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), spotValue.location)
                binding.descriptionContentTV.text = spotValue.description
                binding.spotTypeTV.text = spotValue.spotType
            } else {
                showSnackBar(binding.root, R.string.error__spots_load)
            }
        })
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
        spotDetailFragmentViewModel.imagesUris.observe(this, { imageUris ->
            val adapterItems = mutableListOf<SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem>()
            imageUris.forEach {
                adapterItems.add(SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem(it))
            }
            photosAdapter.injectData(adapterItems)
            binding.photosLoadingV.isVisible = imageUris.isEmpty()
        })

        spotDetailFragmentViewModel.loadImages(arguments.spotArguments.spot?.images ?: listOf())
    }

    private fun setupButtons() {
        binding.navigateB.onClick {
            val spot = spotDetailFragmentViewModel.spot.value?.getValueOrNull()
            spot?.let { nonNullSpot ->
                handler.openSpotInMaps(nonNullSpot)
            }
        }

        binding.addReviewB.onClick {
            // TODO open add review bottom sheet fragment
        }
    }

    override fun onPhotoClick(position: Int) {
        handler.openImagesGallery(spotDetailFragmentViewModel.imagesUris.value ?: listOf(), position)
    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler {
    fun openImagesGallery(images: List<Uri>, position: Int)
    fun openSpotInMaps(spot: Spot)
}