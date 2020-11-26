package com.g3.spot_guide.providers

import android.content.Context
import com.g3.base.either.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.models.User
import com.g3.spot_guide.utils.ImageCompressorUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import java.util.*

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

    suspend fun getUserByEmail(email: String): Either<User> {
        return try {
            val result = collectionReference.whereEqualTo("email", email).get().await()
            val users = result.toObjects(User::class.java)
            if (users.isNotEmpty()) {
                Either.Success(users.first())
            } else {
                Either.Error(null)
            }
        } catch (e: Exception) {
            Either.Error(null)
        }
    }

    suspend fun getUserById(userId: String): Either<User> {
        return try {
            val result = collectionReference.document(userId).get().await()
            val user = result.toObject(User::class.java)
            if (user != null) {
                Either.Success(user)
            } else {
                Either.Error(null)
            }
        } catch (e: Exception) {
            Either.Error(e.message)
        }
    }

    suspend fun saveUser(user: User): Either<User> {
        return try {
            collectionReference.document(user.id).set(user).await()
            Either.Success(user)
        } catch (e: Exception) { Either.Error(null) }
    }

    suspend fun changeProfilePicture(context: Context, newProfilePicture: ImageModel, currentProfilePicturePath: String): Either<String> {
        try {
            val compressedImage = ImageCompressorUtils.compressImage(context, newProfilePicture)
            compressedImage?.let {
                val storageRef = storage.reference
                val storageReferenceString = "profile_pictures/${UUID.randomUUID()}"
                val imageReference = storageRef.child(storageReferenceString)
                val uploadTask = imageReference.putBytes(compressedImage.readBytes())
                uploadTask.await()

                try {
                    storageRef.child(currentProfilePicturePath).delete().await() // deleting old profile picture
                } catch (e: Exception) {

                }

                return Either.Success(storageReferenceString)
            }
            return Either.Error(null)
        } catch (e: Exception) {
            return Either.Error(null)
        }
    }
}