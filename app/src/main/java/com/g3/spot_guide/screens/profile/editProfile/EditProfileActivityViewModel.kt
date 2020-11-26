package com.g3.spot_guide.screens.profile.editProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.models.ImageModel

class EditProfileActivityViewModel : ViewModel() {

    lateinit var activityParams: EditProfileActivity.Parameters

    val profilePicture = MutableLiveData<ImageModel?>()
}