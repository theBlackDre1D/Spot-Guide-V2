package com.g3.spot_guide.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


fun ViewModel.doInCoroutine(action: suspend () -> Unit) {
    this.viewModelScope.launch {
        action()
    }
}