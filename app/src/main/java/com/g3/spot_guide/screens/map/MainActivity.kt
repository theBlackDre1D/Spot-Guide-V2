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
import com.g3.spot_guide.screens.crew.CrewFragmentHandler
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity
import com.g3.spot_guide.screens.profile.myProfile.MyProfileFragmentDirections
import com.g3.spot_guide.screens.profile.myProfile.ProfileFragmentHandler
import com.g3.spot_guide.screens.profile.otherUserProfile.OtherUserProfileActivity
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.g3.spot_guide.utils.InstagramUtils
import com.g3.spot_guide.utils.OpenMapsUtils
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<MainActivityNavBarBinding, Nothing>(), MapFragmentHandler,
    SpotDetailFragmentHandler, FilterSpotsBottomSheetHandler, ProfileFragmentHandler, CrewFragmentHandler {

    private val mapActivityViewModel: MapActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityNavBarBinding = MainActivityNavBarBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityNavBarBinding) {
        setupNavBar()
    }

    override fun openSpotDetailScreen(spot: Spot) {
        val arguments = SpotDetailFragmentArguments(spot, null)
        navController?.navigateSafe(MapFragmentDirections.actionOpenSpotBottomSheet(arguments))
    }

    override fun openAddSpotScreen(latLng: LatLng) {
        val params = AddSpotActivity.Parameters(latLng.latitude, latLng.longitude)
        Session.application.coordinator.startAddSpotActivity(this, params)
    }

    override fun openSpotsFilterSheet() {
        val arguments = FilterSpotsBottomSheetArguments(mapActivityViewModel.spotsFilters.value ?: listOf())
        navController?.navigateSafe(MapFragmentDirections.actionOpenFilterSpotsBottomSheet(arguments))
    }

    override fun getFiltersLiveData(): MutableLiveData<MutableList<SpotType>> = mapActivityViewModel.spotsFilters

    override fun openImagesGallery(images: List<Uri>, position: Int) {
        val params = ImagesPreviewFragmentArguments(images, position)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToImagesPreview(params))
    }

    override fun openSpotInMaps(spot: Spot) {
        OpenMapsUtils.openMapOnLocation(this, spot)
    }

    override fun openAddReviewBottomSheet(spot: Spot) {
//        TODO("Not yet implemented")
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

    private fun openOtherProfileActivity(user: User) {
        val parameters = OtherUserProfileActivity.Parameters(user)
        Session.application.coordinator.startOtherUserProfileActivity(this, parameters)
    }

    override fun openProfileFragment(user: User) = openOtherProfileActivity(user)
    override fun onCrewMemberClick(member: User) = openOtherProfileActivity(member)

    override fun openEditProfileScreen(user: User) {
        val coordinator = Session.application.coordinator
        coordinator.startEditProfileActivity(this, EditProfileActivity.Parameters(user))
    }

    override fun openSpotDetail(spot: Spot) {
        val arguments = SpotDetailFragmentArguments(spot, null)
        navController?.navigateSafe(MyProfileFragmentDirections.actionOpenSpotDetail(arguments))
    }


    override fun onSpotClick(spotId: String) {
        val arguments = SpotDetailFragmentArguments(null, spotId)
        navController?.navigateSafe(MapFragmentDirections.actionOpenSpotBottomSheet(arguments))
    }

    override fun openInstagramAccount(instagramNick: String) {
        InstagramUtils.openInstagramProfile(this, instagramNick)
    }
}