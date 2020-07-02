package com.g3.spot_guide.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.g3.spot_guide.Session
import java.io.Serializable

private const val STATE__BUNDLE_KEY = "STATE__BUNDLE_KEY"

abstract class BaseState : Serializable

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<STATE: BaseState>(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    init {
        this.initializeState()
    }

    var state: MutableLiveData<STATE>
        get() = this.savedState.getLiveData(STATE__BUNDLE_KEY)
        set(value) = this.savedState.set(STATE__BUNDLE_KEY, value)

    abstract fun initializeState()


}