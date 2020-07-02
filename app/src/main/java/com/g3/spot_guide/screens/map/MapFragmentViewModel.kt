package com.g3.spot_guide.screens.map

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session

private const val LOCATION__BUNDLE_KEY = "LOCATION__BUNDLE_KEY"

class MapFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    var lastKnownLocation: Location?
        get() = savedState.get(LOCATION__BUNDLE_KEY)
        set(value) = savedState.set(LOCATION__BUNDLE_KEY, value)

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = MapFragmentViewModel(handle, Session.application) as T
    }
}