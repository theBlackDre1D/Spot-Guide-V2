package com.g3.spot_guide.base

sealed class Either<out T> {
    data class Error(val message: String?) : Either<Nothing>()
    data class Success<T>(val value: T) : Either<T>()

    val isSuccess: Boolean
        get() = this is Success

    fun getSafeSuccess(): Success<out T>? {
        return if (this.isSuccess) this as Success<out T>
        else null
    }
}