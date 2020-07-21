package com.g3.spot_guide.repositories

import com.g3.spot_guide.providers.UserFirestoreProvider

class UserRepository (
    private val provider: UserFirestoreProvider
) {

    suspend fun getUserByEmail(email: String) = provider.getUserByEmails(email)
}

