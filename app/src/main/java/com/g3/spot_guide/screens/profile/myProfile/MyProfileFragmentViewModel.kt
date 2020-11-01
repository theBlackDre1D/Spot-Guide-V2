package com.g3.spot_guide.screens.profile.myProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.SpotRepository
import com.g3.spot_guide.repositories.UserRepository

class MyProfileFragmentViewModel(
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository
) : ViewModel() {

    var userEmail: String? = null

    val userLiveData = MutableLiveData<Either<User>>()
    val userSpots = MutableLiveData<Either<List<Spot>>>()

    fun getUserByEmail(email: String) {
        doInCoroutine {
            val user = userRepository.getUserByEmail(email)
            userLiveData.postValue(user)
        }
    }

    fun getUsersSpots(userId: String) {
        doInCoroutine {
            val spots = spotRepository.getUsersSpots(userId)
            userSpots.postValue(spots)
        }
    }
}