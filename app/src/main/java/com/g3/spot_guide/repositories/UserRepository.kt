package com.g3.spot_guide.repositories

import com.g3.spot_guide.providers.UserFirestoreProvider

class UserRepository (
    private val provider: UserFirestoreProvider
) {

    suspend fun loginUserWithFirebase(email: String, password: String) = provider.loginUserWithFirebase(email, password)
    suspend fun registerUserWithFirebase(email: String, password: String) = provider.registerUserWithFirebase(email, password)
    suspend fun getUserByEmail(email: String) = provider.getUserByEmail(email)
}

