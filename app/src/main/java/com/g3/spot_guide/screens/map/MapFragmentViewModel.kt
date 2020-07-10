package com.g3.spot_guide.screens.map

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.repositories.SpotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MapFragmentViewModel(
    private val repository: SpotRepository
) : ViewModel() {

    var lastKnownLocation: Location? = null

    val spots = MutableLiveData<Either<List<Spot>>>()

    fun getAllSpots() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            val result = repository.getAllSpots()
            spots.postValue(result)
        }
    }
}