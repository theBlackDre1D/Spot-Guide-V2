package com.g3.spot_guide.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginFragmentViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var email: String = ""
    var password: String = ""

    val loggedInUser = MutableLiveData<Either<User>>()

    fun logIn() {
        viewModelScope.launch {
            val result = repository.getUserByEmail(email)
            loggedInUser.postValue(result)
        }
    }
}