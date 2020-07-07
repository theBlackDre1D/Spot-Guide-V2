package com.g3.spot_guide.screens.addSpot

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.repositories.SpotRepository

@Suppress("UNCHECKED_CAST")
class AddSpotFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    private val repository: SpotRepository by lazy { SpotRepository() }

    var groundType: GroundType? = null
    var spotName: String? = null
    var description: String? = null

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = AddSpotFragmentViewModel(handle, Session.application) as T
    }
}