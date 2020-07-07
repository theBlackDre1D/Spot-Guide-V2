package com.g3.spot_guide.screens.test

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session

@Suppress("UNCHECKED_CAST")
class TestFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    var testText = "Init text"

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = TestFragmentViewModel(handle, Session.application) as T
    }
}