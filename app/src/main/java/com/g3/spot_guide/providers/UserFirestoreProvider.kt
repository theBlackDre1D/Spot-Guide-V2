package com.g3.spot_guide.providers

import com.g3.spot_guide.base.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class UserFirestoreProvider : BaseFirestoreProvider(FirestoreEntityName.USERS) {

    suspend fun loginUserWithFirebase(email: String, password: String): Either<FirebaseUser> {
        return try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) Either.Success(firebaseUser) else Either.Error(null)
        } catch (e: Exception) { Either.Error(e.localizedMessage) }
    }

    suspend fun registerUserWithFirebase(email: String, password: String): Either<FirebaseUser> {
        return try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            return if (firebaseUser != null) Either.Success(firebaseUser) else Either.Error(null)
        } catch (e: Exception) { Either.Error(null) }
    }
}