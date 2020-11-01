package com.g3.spot_guide.screens.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.repositories.SpotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MapFragmentViewModel(
    private val repository: SpotRepository
) : ViewModel() {

    var lastKnownLocation: Location? = null
    var mostRecentOpenedSpot: Spot? = null

    val spots = MutableLiveData<Either<List<Spot>>>()

    fun getAllSpots() {
        doInCoroutine {
            viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
                val result = repository.getAllSpots()
                spots.postValue(result)
            }
        }
    }
}