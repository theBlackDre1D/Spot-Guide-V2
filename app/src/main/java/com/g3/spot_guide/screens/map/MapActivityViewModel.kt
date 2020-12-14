package com.g3.spot_guide.screens.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.repositories.UserRepository

class MapActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val spotsFilters = MutableLiveData<MutableList<SpotType>>(mutableListOf())
    val todaySpotLiveData = MutableLiveData<TodaySpot>()

    fun deleteLoggedUserTodaySpot() {
        doInCoroutine {
            userRepository.deleteTodaySpot()
        }
    }
}