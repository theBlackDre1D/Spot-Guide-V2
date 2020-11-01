package com.g3.spot_guide.screens.profile.otherUserProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.SpotRepository
import com.g3.spot_guide.repositories.UserRepository

class OtherUserProfileFragmentViewModel(
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository
) : ViewModel() {

    val user = MutableLiveData<Either<User>>()
    val userSpots = MutableLiveData<Either<List<Spot>>>()

    fun getUserById(userId: String) {
        doInCoroutine {
            val result = userRepository.getUserById(userId)
            user.postValue(result)
        }
    }

    fun getUsersSpots(userId: String) {
        doInCoroutine {
            val spots = spotRepository.getUsersSpots(userId)
            userSpots.postValue(spots)
        }
    }
}