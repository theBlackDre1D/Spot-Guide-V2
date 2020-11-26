package com.g3.spot_guide.screens.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.models.TodaySpot

class MapActivityViewModel : ViewModel() {

    val spotsFilters = MutableLiveData<MutableList<SpotType>>(mutableListOf())
    val todaySpotLiveData = MutableLiveData<TodaySpot>()
}