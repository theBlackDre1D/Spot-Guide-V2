package com.g3.base.either

sealed class Either<out T> {
    data class Error(val message: String?) : Either<Nothing>() {
        constructor(exception: Exception) : this(exception.localizedMessage)
    }
    data class Success<T>(val value: T) : Either<T>()
    object Loading : Either<Nothing>()

    fun handleResult(onSuccess: (Success<out T>) -> Unit, onError: (Error) -> Unit) {
        when (this) {
            is Error -> onError(this)
            is Success -> onSuccess(this)
        }
    }

    fun getValueOrNull(): T? {
        return when (this) {
            is Error, Loading -> null
            is Success -> this.value
        }
    }
}