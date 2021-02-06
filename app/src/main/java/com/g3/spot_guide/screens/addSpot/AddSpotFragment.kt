package com.g3.spot_guide.screens.addSpot

import GeoCoderUtils
import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.base.either.Either
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.AddSpotFragmentBinding
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.afterTextChanged
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonsView
import com.g3.spot_guide.views.SliderRatingView
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel


const val ADD_PHOTO__REQUEST_CODE = 100


class AddSpotFragment : BaseFragment<AddSpotFragmentBinding, AddSpotFragmentHandler>(), PhotosAdapter.PhotosAdapterHandler, AppBarView.AppBarViewHandler {

    private val photosAdapter: PhotosAdapter by lazy { PhotosAdapter(this) }

    private val addSpotFragmentViewModel: AddSpotFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): AddSpotFragmentBinding = AddSpotFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: AddSpotFragmentBinding, context: Context) {
        saveLocationData()
        setupAppBar()
        setupBottomButtons()
        setupGroundRating()
        setupLocationName()
        setupPhotosSection()
        setupSpotCategorySection()
        setupListeners()
    }

    private fun setupAppBar() {
        val configuration = AppBarView.AppBarViewConfiguration(R.string.add_spot__header, true,null, this)
        binding.appBarV.configuration = configuration
    }

    private fun setupBottomButtons() {
        val listener = object : BottomButtonsView.BottomButtonsViewListener {
            override fun onLeftButtonClick() = handler.navigateBack()
            override fun onRightButtonClick() {
                addSpotFragmentViewModel.uploadSpotResult.observe(this@AddSpotFragment, Observer { result ->
                    when(result) {
                        is Either.Error -> {
                            showSnackBar(binding.root, R.string.error__spot_upload)
                            handler.showLoading(false)
                        }
                        is Either.Success<*> -> handler.navigateBack()
                    }
                })

                addSpotFragmentViewModel.uploadSpot(requireContext(), handler.getPickedImages().value ?: listOf())
                handler.showLoading(true)
            }
    }

        val configuration = BottomButtonsView.BottomButtonsViewConfiguration(R.string.add_spot__cancel, R.string.add_spot__confirm, listener)

        binding.bottomButtonsV.configuration = configuration
    }

    private fun setupGroundRating() {
        val listener = object : SliderRatingView.SliderRatingViewListener {
            override fun onSliderValueChanged(newValue: Int) {
                val groundType = when(newValue) {
                    GroundType.BAD.ordinal -> GroundType.BAD
                    GroundType.GOOD.ordinal -> GroundType.GOOD
                    GroundType.HORRIBLE.ordinal -> GroundType.HORRIBLE
                    GroundType.OK.ordinal -> GroundType.OK
                    else -> GroundType.PERFECT
                }
                addSpotFragmentViewModel.groundType = groundType
            }
        }

        val configuration = SliderRatingView.SliderRatingViewConfiguration(addSpotFragmentViewModel.groundType, listener)
        binding.groundQualityRatingV.configuration = configuration
    }

    private fun saveLocationData() {
        val locationData = handler.getLocationData()
        addSpotFragmentViewModel.locationData = LatLng(locationData.latitude, locationData.longitude)
    }

    private fun setupLocationName() {
        binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), addSpotFragmentViewModel.locationData)
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
            photosAdapter.injectData(items)
        })

    }

    private fun setupSpotCategorySection() {
        binding.chooseTypeB.onClick {
            handler.openSpotTypeDialog()
        }

        binding.choosedTypeB.onClick {
            handler.openSpotTypeDialog()
        }

        handler.getSpotTypeLiveData().observe(this, Observer { spotType ->
            addSpotFragmentViewModel.spotType = spotType
            binding.choosedTypeB.text = spotType.spotName

            binding.chooseTypeB.isInvisible = spotType != null
            binding.choosedTypeB.isInvisible = spotType == null
        })
    }

    private fun setupListeners() {
        binding.spotNameET.afterTextChanged {
            addSpotFragmentViewModel.spotName = it
        }

        binding.descriptionET.afterTextChanged {
            addSpotFragmentViewModel.description = it
        }

        binding.ratingV.setOnRatingBarChangeListener { _, rating, _ ->
            addSpotFragmentViewModel.spotRating = rating.toInt()
        }

        binding.parkingS.setOnCheckedChangeListener { _, isChecked ->
            addSpotFragmentViewModel.parking = isChecked
        }
    }

    override fun onDeletePhoto(imageModel: ImageModel) {
        handler.onDeletePhoto(imageModel)
    }

    override fun onLeftIconClick() {
        handler.navigateBack()
    }
}

interface AddSpotFragmentHandler : BaseFragmentHandler {
    fun navigateBack()
    fun openGallery(requestCode: Int)
    fun openSpotTypeDialog()
    fun getPickedImages(): MutableLiveData<List<ImageModel>>
    fun onDeletePhoto(imageModel: ImageModel)
    fun getLocationData(): AddSpotActivity.Parameters
    fun getSpotTypeLiveData(): MutableLiveData<SpotType>
    fun showLoading(show: Boolean)
}