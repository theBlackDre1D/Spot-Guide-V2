package com.g3.base.repository

import com.g3.base.either.Either

abstract class BaseRepository {
    protected suspend fun <T> runTask(task: suspend () -> T): Either<T> {
        return try {
            val result = task()
            return Either.Success(result)
        } catch (e: Exception) {
            Either.Error(e.localizedMessage)
        }
    }
}