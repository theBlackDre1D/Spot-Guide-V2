package studio.bluecrystal.screens.map

import android.location.Location
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import studio.bluecrystal.Session
import studio.bluecrystal.base.BaseState
import studio.bluecrystal.base.BaseViewModel

@Suppress("UNCHECKED_CAST")
class MapFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : BaseViewModel<MapFragmentViewModel.State>(savedState, session) {

    data class State(
        val text: String = "",
        val lastKnowLocation: Location? = null
    ) : BaseState()

    override fun initializeState() {
        state.postValue(State())
    }

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = MapFragmentViewModel(handle, Session.application) as T
    }
}