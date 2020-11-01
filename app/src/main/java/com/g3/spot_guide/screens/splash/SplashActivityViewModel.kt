package com.g3.spot_guide.screens.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.UserRepository

class SplashActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val user = MutableLiveData<User>()

    fun getUser(email: String) {
        doInCoroutine {
            val result = userRepository.getUserByEmail(email)
            result.getValueOrNull()?.let {
                user.postValue(it)
            }
        }
    }
}