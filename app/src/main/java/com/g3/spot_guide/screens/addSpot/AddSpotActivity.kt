package com.g3.spot_guide.screens.addSpot

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.g3.base.screens.activity.BaseActivity
import com.g3.base.screens.activity.BaseParameters
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.AddSpotActivityBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.screens.gallery.GalleryFragmentHandler
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable


class AddSpotActivity : BaseActivity<AddSpotActivityBinding, AddSpotActivity.Arguments>(),  AddSpotFragmentHandler,
    GalleryFragmentHandler, ChooseSpotTypeBottomSheetHandler {

    companion object {
        const val ADD_SPOT_PARAMETERS__EXTRAS_KEY = "ADD_SPOT_PARAMETERS__EXTRAS_KEY"
    }

    private val addSpotActivityViewModel: AddSpotActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.addSpotNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): AddSpotActivityBinding = AddSpotActivityBinding.inflate(layoutInflater)
    override fun createParameters() = Arguments()
    override fun onActivityLoadingFinished(binding: AddSpotActivityBinding) {}

    inner class Arguments : BaseParameters {
        override fun loadParameters(extras: Bundle) {
            addSpotActivityViewModel.activityParams = extras.getSerializable(ADD_SPOT_PARAMETERS__EXTRAS_KEY) as Parameters
        }
    }

    data class Parameters(
        val latitude: Double,
        val longitude: Double
    ) : Serializable

    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun getPickedImages(): MutableLiveData<List<ImageModel>> {
        return addSpotActivityViewModel.pickedImages
    }

    override fun savePickedImages(images: List<ImageModel>) {
        addSpotActivityViewModel.pickedImages.postValue(images)
    }

    override fun onDeletePhoto(imageModel: ImageModel) {
        val currentImages = addSpotActivityViewModel.pickedImages.value?.toMutableList()
        currentImages?.let {
            currentImages.remove(imageModel)
            addSpotActivityViewModel.pickedImages.postValue(currentImages)
        }
    }

    override fun openGallery(requestCode: Int) {
        navController?.navigateSafe(AddSpotFragmentDirections.actionAddSpotToGalleryFragment())
    }

    override fun openSpotTypeDialog() {
        navController?.navigateSafe(AddSpotFragmentDirections.actionAddSpotToChooseSpotTypeBottomSheet())
    }

    override fun getLocationData(): Parameters {
        return addSpotActivityViewModel.activityParams
    }

    override fun getSpotTypeLiveData(): MutableLiveData<SpotType> {
        return addSpotActivityViewModel.spotType
    }

    override fun showLoading(show: Boolean) {
        binding.loadingV.isVisible = show
    }

    override fun onTypePick(type: SpotType) {
        addSpotActivityViewModel.spotType.postValue(type)
    }
}