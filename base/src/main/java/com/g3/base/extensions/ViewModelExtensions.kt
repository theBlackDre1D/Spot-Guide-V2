package com.g3.base.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> ViewModel.flowTask(task: suspend () -> T): Flow<T> {
    return flow {
        val result = task()
        emit(result)
    }
}

fun ViewModel.coroutineTask(task: suspend () -> Unit) {
    viewModelScope.launch { task() }
}

fun ViewModel.coroutineTask(dispatcher: CoroutineDispatcher, task: suspend () -> Unit) {
    viewModelScope.launch(dispatcher) { task() }
}