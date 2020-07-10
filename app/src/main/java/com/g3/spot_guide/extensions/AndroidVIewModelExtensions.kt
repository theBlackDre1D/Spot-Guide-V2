package com.g3.spot_guide.extensions

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


fun AndroidViewModel.doInCoroutine(action: suspend () -> Unit) {
    this.viewModelScope.launch {
        action()
    }
}