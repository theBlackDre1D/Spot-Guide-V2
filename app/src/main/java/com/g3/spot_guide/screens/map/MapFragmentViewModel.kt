package com.g3.spot_guide.screens.map

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.repositories.SpotRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
class MapFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    private val repository: SpotRepository by lazy { SpotRepository() }

    var lastKnownLocation: Location? = null

    val spots = MutableLiveData<Either<List<Spot>>>()

    fun getAllSpots() {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            val result = repository.getAllSpots()
            spots.postValue(result)
        }
    }

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = MapFragmentViewModel(handle, Session.application) as T
    }
}