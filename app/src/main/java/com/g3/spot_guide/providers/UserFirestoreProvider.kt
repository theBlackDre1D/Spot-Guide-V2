package com.g3.spot_guide.providers

import com.g3.spot_guide.base.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.User
import kotlinx.coroutines.tasks.await

class UserFirestoreProvider : BaseFirestoreProvider(FirestoreEntityName.USERS) {

    suspend fun getUserByEmails(email: String): Either<User> {
        return try {
            val result = collectionReference.whereEqualTo("email", email).get().await()
            val user = result.toObjects(User::class.java).firstOrNull()
            if (user != null)  Either.Success(user)
            else Either.Error(null)

        } catch (e: Exception) { Either.Error(null) }
    }
}