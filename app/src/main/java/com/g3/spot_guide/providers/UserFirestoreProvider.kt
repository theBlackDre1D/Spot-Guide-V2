package com.g3.spot_guide.providers

import android.content.Context
import com.g3.base.either.Either
import com.g3.spot_guide.Session
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.models.MemberRequestDecision
import com.g3.spot_guide.models.TodaySpot
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
            if (firebaseUser != null) {
                Either.Success(firebaseUser)
            } else {
                Either.Error(null)
            }
        } catch (e: Exception) {
            Either.Error(e.localizedMessage)
        }
    }

    suspend fun registerUserWithFirebase(email: String, password: String, userName: String): Either<FirebaseUser> {
        return try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            return if (firebaseUser != null) {
                val newUser = User(nick = userName, email = email)
                val userCreationResult = createUser(newUser)
                return if (userCreationResult.getValueOrNull() != null) {
                    loginUserWithFirebase(email, password)
                } else {
                    Either.Error(null)
                }
            } else {
                Either.Error(null)
            }
        } catch (e: Exception) {
            Either.Error(null)
        }
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
        } catch (e: Exception) {
            Either.Error(null)
        }
    }

    suspend fun createUser(user: User): Either<User> {
        return try {
            collectionReference.document().set(user.toUploadModel()).await()
            Either.Success(user)
        } catch (e: Exception) {
            Either.Error(null)
        }
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
                } catch (e: Exception) {}

                return Either.Success(storageReferenceString)
            }
            return Either.Error(null)
        } catch (e: Exception) {
            return Either.Error(null)
        }
    }

    suspend fun addTodaySpotToCurrentUser(newTodaySpot: TodaySpot): Either<TodaySpot> {
        try {
            val currentUser = Session.loggedInUser
            currentUser?.let { user ->
                val userToUpload = user.copy(todaySpot = newTodaySpot)
                collectionReference.document(user.id).set(userToUpload).await()
                return Either.Success(newTodaySpot)
            }
            return Either.Error(null)
        } catch (e: Exception) {
            return Either.Error(null)
        }
    }

    suspend fun deleteTodaySpot(): Either<Unit> {
        try {
            val currentUser = Session.loggedInUser
            currentUser?.let { user ->
                val userToUpload = user.copy(todaySpot = null)
                collectionReference.document(user.id).set(userToUpload).await()
                return Either.Success(Unit)
            }
            return Either.Error("deleteTodaySpot : Current user is null")
        } catch (e: Exception) {
            return Either.Error("deleteTodaySpot : Today spot delete error")
        }
    }

    suspend fun onCrewMemberRequestDecision(accept: Boolean, requestUserId: String): Either<MemberRequestDecision> {
        try {
            val otherUser = getUserById(requestUserId).getValueOrNull()
            val currentUser = Session.loggedInUser
            if (currentUser != null && otherUser != null) {
                if (accept) {
                    val remainingMemberRequests = currentUser.memberRequests.filter { it != requestUserId }
                    val currentUserNewCrewMemberList = mutableListOf<String>()
                    currentUserNewCrewMemberList.addAll(currentUser.friends)
                    currentUserNewCrewMemberList.add(requestUserId)
                    val currentUserWithChanges = currentUser.copy(memberRequests = remainingMemberRequests, friends = currentUserNewCrewMemberList)

                    val otherUserCrewMembers = mutableListOf<String>()
                    otherUserCrewMembers.addAll(otherUser.friends)
                    otherUserCrewMembers.add(currentUser.id)
                    val otherUserWithChanges = otherUser.copy(friends = otherUserCrewMembers)

                    collectionReference.document(currentUserWithChanges.id).set(currentUserWithChanges).await()
                    collectionReference.document(otherUserWithChanges.id).set(otherUserWithChanges).await()

                    Session.saveAndSetLoggedInUser(currentUserWithChanges)
                    return Either.Success(MemberRequestDecision(accept, requestUserId))
                } else {
                    return Either.Error(null)
                }
            } else {
                return Either.Error(null)
            }
        } catch (e: Exception) {
            return Either.Error("onCrewMemberRequestDecision : Error when handling crew member request")
        }
    }

    suspend fun getCrewMembersForThisSpot(spotId: String): Either<List<User>> {
        try {
            val loggedUser = Session.loggedInUser
            if (loggedUser != null) {
                val crewMembers = mutableListOf<User>()
                loggedUser.friends.forEach { crewMemberId ->
                    val userEither = getUserById(crewMemberId)
                    userEither.getValueOrNull()?.let { user ->
                        crewMembers.add(user)
                    }
                }

                val crewMembersForSpot = crewMembers.filter { it.todaySpot?.spotId == spotId }

                return Either.Success(crewMembersForSpot)
            } else {
                return Either.Error("getCrewMembersForThisSpot : Logged user null")
            }
        } catch (e: Exception) {
            return Either.Error("getCrewMembersForThisSpot : Error when loading crew members for current spot")
        }
    }
}