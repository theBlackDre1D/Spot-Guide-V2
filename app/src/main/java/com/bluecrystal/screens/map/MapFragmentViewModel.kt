package com.bluecrystal.screens.map

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.bluecrystal.Session
import com.bluecrystal.base.BaseState
import com.bluecrystal.base.BaseViewModel

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : BaseViewModel<MapFragmentViewModel.State>(savedState, session) {

    init {
        state.value = State()
    }

    data class State(
        var text: String = "",
        var lastKnowLocation: Location? = null
    ) : BaseState()

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = MapFragmentViewModel(handle, Session.application) as T
    }
}