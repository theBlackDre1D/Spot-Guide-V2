package com.g3.spot_guide.screens.map

import android.net.Uri
import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.setupWithNavController
import com.g3.base.screens.activity.BaseActivity
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.MainActivityNavBarBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.profile.ProfileFragmentDirections
import com.g3.spot_guide.screens.profile.ProfileFragmentHandler
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<MainActivityNavBarBinding, Nothing>(), MapFragmentHandler,
    SpotDetailFragmentHandler, FilterSpotsBottomSheetHandler, ProfileFragmentHandler {

    private val mapActivityViewModel: MapActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityNavBarBinding = MainActivityNavBarBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityNavBarBinding) {
        setupNavBar()
    }

    override fun openSpotDetailScreen(spot: Spot) {
        navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToSpotDetailFragment(spot))
    }

    override fun openAddSpotScreen(latLng: LatLng) {
        val params = AddSpotActivity.Parameters(latLng.latitude, latLng.longitude)
        Session.application.coordinator.startAddSpotActivity(this, params)
    }

    override fun openSpotsFilterSheet() {
        val arguments = FilterSpotsBottomSheetArguments(mapActivityViewModel.spotsFilters.value ?: listOf())
        navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToFilterSpotsBottomSheet(arguments))
    }

    override fun getFiltersLiveData(): MutableLiveData<MutableList<SpotType>> = mapActivityViewModel.spotsFilters

    override fun openImagesGallery(images: List<Uri>, position: Int) {
        val params = ImagesPreviewFragmentArguments(images, position)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToImagesPreview(params))
    }

    override fun onSpotTypeCLick(spotType: SpotType) {
        val currentFiltersValue = mapActivityViewModel.spotsFilters.value
        if (currentFiltersValue != null) {
            if (currentFiltersValue.contains(spotType)) {
                currentFiltersValue.remove(spotType)
            } else {
                currentFiltersValue.add(spotType)
            }
        }
        mapActivityViewModel.spotsFilters.postValue(currentFiltersValue)
    }

    private fun setupNavBar() {
        navController?.let {
            binding.bottomNavV.setupWithNavController(it)
        }
    }

    override fun openEditProfileScreen(user: User) {
        val coordinator = Session.application.coordinator
        coordinator.startEditProfileActivity(this, EditProfileActivity.Parameters(user))
    }

    override fun openProfileFragment(user: User) {
        // TODO("Not yet implemented")
    }

    override fun openSpotDetail(spot: Spot) {
        navController?.navigateSafe(ProfileFragmentDirections.actionOpenSpotDetail(spot))
    }
}