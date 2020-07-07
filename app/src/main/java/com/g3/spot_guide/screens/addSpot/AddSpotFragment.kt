package com.g3.spot_guide.screens.addSpot

import GeoCoderUtils
import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.spot_guide.R
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.AddSpotFragmentBinding
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonsView
import com.g3.spot_guide.views.EmojiRatingView
import com.google.android.gms.maps.model.LatLng


const val ADD_PHOTO__REQUEST_CODE = 100


class AddSpotFragment : BaseFragment<AddSpotFragmentBinding, AddSpotFragmentViewModel, AddSpotFragmentHandler>(), ChooseSpotTypeBottomSheetHandler, PhotosAdapter.PhotosAdapterHandler, AppBarView.AppBarViewHandler {

    private val photosAdapter: PhotosAdapter by lazy { PhotosAdapter(this) }

    override val viewModel: AddSpotFragmentViewModel by viewModels { AddSpotFragmentViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): AddSpotFragmentBinding = AddSpotFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: AddSpotFragmentBinding, context: Context) {
        setupAppBar()
        setupBottomButtons()
        setupGroundEmojiRating()
        setupLocationName()
        setupPhotosSection()
    }

    private fun setupAppBar() {
        val configuration = AppBarView.AppBarViewConfiguration(R.string.add_spot__header, true, this)
        binding.appBarV.configuration = configuration
    }

    private fun setupBottomButtons() {
        val listener = object : BottomButtonsView.BottomButtonsViewListener {
            override fun onLeftButtonClick() {
                handler.navigateBack()
            }

            override fun onRightButtonClick() {
                // TODO
            }
        }

        val configuration = BottomButtonsView.BottomButtonsViewConfiguration(R.string.add_spot__cancel, R.string.add_spot__confirm, listener)

        binding.bottomButtonsV.configuration = configuration
    }

    private fun setupGroundEmojiRating() {
        val listener = object : EmojiRatingView.EmojiRatingViewListener {
            override fun onEmojiClick(type: GroundType) {
                // TODO
            }
        }

        binding.groundQualityRatingV.listener = listener
    }

    private fun setupLocationName() {
        val locationData = handler.getLocationData()
        val latLng = LatLng(locationData.latitude, locationData.longitude)
        binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), latLng)
    }

    private fun setupPhotosSection() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        binding.photosRV.adapter = photosAdapter
        binding.photosRV.layoutManager = layoutManager
        binding.photosRV.setHasFixedSize(true)
        binding.photosRV.addOnScrollListener(CenterScrollListener())

        binding.addPhotosIV.onClick {
            handler.openGallery(ADD_PHOTO__REQUEST_CODE)
        }

        val liveData = handler.getPickedImages()
        liveData.observe(this, Observer { images ->
            binding.photosRV.isVisible = images.isNotEmpty()
            val items = mutableListOf<PhotosAdapter.PhotosAdapterItem>()
            images.forEach { bitmap ->
                items.add(PhotosAdapter.PhotosAdapterItem(bitmap))
            }
            photosAdapter.injectItems(items)
        })

    }

    override fun onTypePick(type: SpotType) {
        // TODO
    }

    override fun onPhotoClick(imageModel: ImageModel) {
        // TODO
    }

    override fun onDeletePhoto(imageModel: ImageModel) {
        handler.onDeletePhoto(imageModel)
    }

    override fun onBackArrowClick() {
        handler.navigateBack()
    }
}

interface AddSpotFragmentHandler : BaseFragmentHandler {
    fun navigateBack()
    fun openGallery(requestCode: Int)
    fun getPickedImages(): MutableLiveData<List<ImageModel>>
    fun onDeletePhoto(imageModel: ImageModel)
    fun getLocationData(): AddSpotActivity.Parameters
}