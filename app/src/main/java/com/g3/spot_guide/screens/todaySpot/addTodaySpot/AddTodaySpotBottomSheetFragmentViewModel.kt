package com.g3.spot_guide.screens.todaySpot.addTodaySpot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.repositories.UserRepository

class AddTodaySpotBottomSheetFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var hours: String = "00"
    var minutes: String = "00"

    val todaySpotAdded = MutableLiveData<Either<TodaySpot>>()

    fun addTodaySpotToCurrentUser(newTodaySpot: TodaySpot) {
        doInCoroutine {
            val result = userRepository.addTodaySpotToCurrentUser(newTodaySpot)
            todaySpotAdded.postValue(result)
        }
    }
}