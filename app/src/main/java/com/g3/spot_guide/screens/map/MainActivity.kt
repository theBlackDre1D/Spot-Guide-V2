package com.g3.spot_guide.screens.map

import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ui.setupWithNavController
import com.g3.base.screens.activity.BaseActivity
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.MainActivityNavBarBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.eventBus.EventBus
import com.g3.spot_guide.eventBus.EventBusListener
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.crew.CrewFragmentDirections
import com.g3.spot_guide.screens.crew.CrewFragmentHandler
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity
import com.g3.spot_guide.screens.profile.myProfile.MyProfileFragmentDirections
import com.g3.spot_guide.screens.profile.myProfile.ProfileFragmentHandler
import com.g3.spot_guide.screens.profile.otherUserProfile.OtherUserProfileActivity
import com.g3.spot_guide.screens.settings.SettingsFragmentHandler
import com.g3.spot_guide.screens.spotCrewMembers.SpotCrewMembersBottomSheetParams
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.g3.spot_guide.screens.todaySpot.TodaySpotBottomSheetFragmentDirections
import com.g3.spot_guide.screens.todaySpot.TodaySpotBottomSheetFragmentHandler
import com.g3.spot_guide.screens.todaySpot.addTodaySpot.AddTodaySpotBottomSheetFragmentArguments
import com.g3.spot_guide.screens.todaySpot.addTodaySpot.AddTodaySpotBottomSheetFragmentHandler
import com.g3.spot_guide.utils.DateUtils
import com.g3.spot_guide.utils.InstagramUtils
import com.g3.spot_guide.utils.OpenMapsUtils
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<MainActivityNavBarBinding, Nothing>(), MapFragmentHandler,
    SpotDetailFragmentHandler, FilterSpotsBottomSheetHandler, ProfileFragmentHandler, CrewFragmentHandler,
    TodaySpotBottomSheetFragmentHandler, AddTodaySpotBottomSheetFragmentHandler, EventBusListener,
    SettingsFragmentHandler {

    private val mapActivityViewModel: MapActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityNavBarBinding = MainActivityNavBarBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityNavBarBinding) {
        setupNavBar()
        deleteTodaySpotIfNotActual()

        EventBus.subscribeOnEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.unsubscribe(this)
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

    override fun fromSpotDetailToAddTodaySpot(spot: Spot, time: String?) {
        val arguments = AddTodaySpotBottomSheetFragmentArguments(spot, time)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToAddTodaySpot(arguments))
    }

    override fun getTodaySpotLiveData(): LiveData<TodaySpot> {
        return mapActivityViewModel.todaySpotLiveData
    }

    override fun fromSpotDetailToSpotCrewMembers(spotCrewMembers: List<User>) {
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToSpotCrewMembers(SpotCrewMembersBottomSheetParams(spotCrewMembers)))
    }

    override fun fromSpotDetailToAddSpotReview(spot: Spot) {
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToAddSpotReview(spot))
    }

    override fun fromSpotDetailBottomSheetToAddSpotReview(spot: Spot) {
        val handler = Handler()
        handler.postDelayed({
            navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToAddSpotReview(spot))
        }, 1)
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

    private fun deleteTodaySpotIfNotActual() {
        val user = Session.loggedInUser
        user?.let { user ->
            if (!DateUtils.isTodaySpotValid(user.todaySpot)) {
                mapActivityViewModel.deleteLoggedUserTodaySpot()
            }
        }
    }

    override fun openProfileFragment(user: User) = openOtherProfileActivity(user)
    override fun onCrewMemberClick(member: User) = openOtherProfileActivity(member)

    override fun openTodaySpotBottomSheet(todaySpot: TodaySpot) {
        navController?.navigateSafe(CrewFragmentDirections.actionCrewFragmentToTodaySpotBottomSheet(todaySpot))
    }

    override fun openEditProfileScreen(user: User) {
        val coordinator = Session.application.coordinator
        coordinator.startEditProfileActivity(this, EditProfileActivity.Parameters(user), false)
    }

    override fun openSpotDetail(spot: Spot) {
        val arguments = SpotDetailFragmentArguments(spot, null)
        navController?.navigateSafe(MyProfileFragmentDirections.actionOpenSpotDetail(arguments))
    }

    override fun openInstagramAccount(instagramNick: String) {
        InstagramUtils.openInstagramProfile(this, instagramNick)
    }

    override fun fromMyProfileToTodaySpot(todaySpot: TodaySpot) {
        navController?.navigateSafe(MyProfileFragmentDirections.actionProfileFragmentToSpotDetailBottomSheet(todaySpot))
    }

    override fun openSpotDetailFromTodaySpotBottomSheet(spotId: String) {
        val arguments = SpotDetailFragmentArguments(null, spotId)
        navController?.navigateSafe(TodaySpotBottomSheetFragmentDirections.actionTodaySpotBottomSheetToSpotDetailFragment(arguments))
    }

    override fun saveTodaySpot(newTodaySpot: TodaySpot) {
        mapActivityViewModel.todaySpotLiveData.postValue(newTodaySpot)
    }

    override fun onEventPosted(event: EventBus.Event) {
        when (event) {
            is EventBus.Event.SpotReviewAdded -> {
                event.spot?.let { spot ->
                    delayOpenSpotDetail(spot)
                }
            }
            is EventBus.Event.SpotReviewDismissed -> {
                delayOpenSpotDetail(event.spot)
            }
        }
    }

    private fun delayOpenSpotDetail(spot: Spot) {
        Handler().postDelayed({
            openSpotDetailScreen(spot)
        }, 200)
    }

    override fun fromSettingsToLogin() {
        Session.application.coordinator.startLoginActivity(this)
    }
}