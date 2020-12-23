package com.g3.spot_guide.screens.profile.otherUserProfile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import com.g3.base.screens.activity.BaseActivity
import com.g3.base.screens.activity.BaseParameters
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.OtherUserProfileActivityBinding
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.g3.spot_guide.screens.todaySpot.addTodaySpot.AddTodaySpotBottomSheetFragmentArguments
import com.g3.spot_guide.screens.todaySpot.addTodaySpot.AddTodaySpotBottomSheetFragmentHandler
import com.g3.spot_guide.utils.InstagramUtils
import com.g3.spot_guide.utils.OpenMapsUtils
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

const val USER_PARAMETERS__EXTRAS_KEY = "USER_PARAMETERS__EXTRAS_KEY"

class OtherUserProfileActivity : BaseActivity<OtherUserProfileActivityBinding, OtherUserProfileActivity.Arguments>(), OtherUserProfileFragmentHandler, SpotDetailFragmentHandler,
    AddTodaySpotBottomSheetFragmentHandler {

    inner class Arguments : BaseParameters {
        override fun loadParameters(extras: Bundle) {
            otherUserProfileActivityViewModel.activityParams = extras.getSerializable(USER_PARAMETERS__EXTRAS_KEY) as Parameters
        }
    }

    data class Parameters(
        val user: User
    ) : Serializable

    private val otherUserProfileActivityViewModel: OtherUserProfileActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.otherUserFragmentNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater) = OtherUserProfileActivityBinding.inflate(layoutInflater)
    override fun createParameters() = Arguments()
    override fun onActivityLoadingFinished(binding: OtherUserProfileActivityBinding) {

    }

    override fun getUser() = otherUserProfileActivityViewModel.activityParams.user


    override fun openInstagramAccount(instagramNick: String) {
        InstagramUtils.openInstagramProfile(this, instagramNick)
    }

    override fun openSpotDetailScreen(spot: Spot) {
        val arguments = SpotDetailFragmentArguments(spot, null)
        navController?.navigateSafe(OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToSpotDetailFragment(arguments))
    }

    override fun fromMyProfileToTodaySpot(todaySpot: TodaySpot) {
        navController?.navigateSafe(OtherUserProfileFragmentDirections.actionOtherUserProfileFragmentToTodaySpotBottomSheet(todaySpot))
    }

    override fun openImagesGallery(images: List<Uri>, position: Int) {
        val arguments = ImagesPreviewFragmentArguments(images, position)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToImagesPreview(arguments))
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
        return otherUserProfileActivityViewModel.todaySpotLiveData
    }

    override fun saveTodaySpot(newTodaySpot: TodaySpot) {
        otherUserProfileActivityViewModel.todaySpotLiveData.postValue(newTodaySpot)
    }
}