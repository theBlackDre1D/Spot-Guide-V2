package com.g3.spot_guide.screens.addSpot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.models.ImageModel

@Suppress("UNCHECKED_CAST")
class AddSpotActivityViewModel : ViewModel() {

    lateinit var activityParams: AddSpotActivity.Parameters

    val pickedImages = MutableLiveData<List<ImageModel>>(mutableListOf())
    var spotType = MutableLiveData<SpotType>()
}