package com.g3.spot_guide.screens.profile.editProfile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.Session
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.UserRepository

class EditProfileFragmentViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val userSaved = MutableLiveData<Either<User>>()

    var nick: String = ""
    var fullName: String = ""
    var stance: String = ""
    var aboutMe: String = ""
    var sponsors: String = ""
    var instagramNick: String? = null

    fun fillAttributesWithValues(user: User) {
        nick = user.nick
        fullName = user.fullName
        stance = user.stance
        aboutMe = user.aboutMe
        sponsors = user.sponsors
        instagramNick = user.instagramNick
    }

    fun saveUser() {
        doInCoroutine {
            val loggedInUser = Session.loggedInUser
            loggedInUser?.let { user ->
                val userForUpload = user.copy(
                    nick = nick,
                    fullName = fullName,
                    stance = stance,
                    aboutMe = aboutMe,
                    sponsors = sponsors,
                    instagramNick = instagramNick
                )

                val result = userRepository.saveUser(userForUpload)
                userSaved.postValue(result)
            }
        }
    }
}