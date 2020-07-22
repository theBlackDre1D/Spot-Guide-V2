package com.g3.spot_guide.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.repositories.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterFragmentViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var userName: String = ""
    var email: String = ""
    var password: String = ""

    val registerResult = MutableLiveData<Either<FirebaseUser>>()

    fun registerUser() {
        viewModelScope.launch {
            val result = repository.registerUserWithFirebase(email, password)
            registerResult.postValue(result)
        }
    }
}