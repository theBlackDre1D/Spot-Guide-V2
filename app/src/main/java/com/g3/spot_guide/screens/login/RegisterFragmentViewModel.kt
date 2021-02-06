package com.g3.spot_guide.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser

class RegisterFragmentViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var userName: String = ""
    var email: String = ""
    var password: String = ""

    val registerResult = MutableLiveData<Either<FirebaseUser>>()
    val userAfterRegister = MutableLiveData<Either<User>>()

    fun registerUser() {
        doInCoroutine {
            val result = repository.registerUserWithFirebase(email, password, userName)
            registerResult.postValue(result)
        }
    }

    fun getCurrentUser() {
        doInCoroutine {
            val result = repository.getUserByEmail(email)
            userAfterRegister.postValue(result)
        }
    }
}