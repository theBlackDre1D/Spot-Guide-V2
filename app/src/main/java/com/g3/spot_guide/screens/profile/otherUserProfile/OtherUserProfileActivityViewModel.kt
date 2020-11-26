package com.g3.spot_guide.screens.profile.otherUserProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.models.TodaySpot

class OtherUserProfileActivityViewModel : ViewModel() {

    lateinit var activityParams: OtherUserProfileActivity.Parameters

    val todaySpotLiveData = MutableLiveData<TodaySpot>()
}