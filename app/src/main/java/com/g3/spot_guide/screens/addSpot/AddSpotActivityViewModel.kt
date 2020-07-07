package com.g3.spot_guide.screens.addSpot

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.repositories.SpotRepository

@Suppress("UNCHECKED_CAST")
class AddSpotActivityViewModel(val state: SavedStateHandle, application: Session) : AndroidViewModel(application) {

    private val repository: SpotRepository by lazy { SpotRepository() }

    lateinit var activityParams: AddSpotActivity.Parameters

    val pickedImages = MutableLiveData<List<ImageModel>>(mutableListOf())
    var groundType: GroundType? = null
    var spotName: String? = null
    var description: String? = null
    var spotType = MutableLiveData<SpotType>()

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = AddSpotActivityViewModel(handle, Session.application) as T
    }
}