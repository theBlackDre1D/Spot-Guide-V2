package com.g3.spot_guide.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser

class LoginFragmentViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var email: String = ""
    var password: String = ""

    val loggedInUser = MutableLiveData<Either<FirebaseUser>>()

    fun logIn() {
        doInCoroutine {
            val result = repository.loginUserWithFirebase(email, password)
            loggedInUser.postValue(result)
        }
    }
}