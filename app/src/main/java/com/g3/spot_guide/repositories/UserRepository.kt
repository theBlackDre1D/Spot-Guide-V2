package com.g3.spot_guide.repositories

import android.content.Context
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.providers.UserFirestoreProvider

class UserRepository (
    private val provider: UserFirestoreProvider
) {

    suspend fun loginUserWithFirebase(email: String, password: String) = provider.loginUserWithFirebase(email, password)
    suspend fun registerUserWithFirebase(email: String, password: String, userName: String) = provider.registerUserWithFirebase(email, password, userName)
    suspend fun getUserByEmail(email: String) = provider.getUserByEmail(email)
    suspend fun getUserById(userId: String) = provider.getUserById(userId)
    suspend fun saveUser(user: User) = provider.saveUser(user)
    suspend fun changeProfilePicture(context: Context, newProfilePicture: ImageModel, currentProfilePicturePath: String) = provider.changeProfilePicture(context, newProfilePicture, currentProfilePicturePath)
    suspend fun addTodaySpotToCurrentUser(newTodaySpot: TodaySpot) = provider.addTodaySpotToCurrentUser(newTodaySpot)
    suspend fun deleteTodaySpot() = provider.deleteTodaySpot()
    suspend fun onCrewMemberRequestDecision(accept: Boolean, requestUserId: String) = provider.onCrewMemberRequestDecision(accept, requestUserId)
    suspend fun getCrewMembersForThisSpot(spotId: String) = provider.getCrewMembersForThisSpot(spotId)
}

