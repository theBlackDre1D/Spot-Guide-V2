package com.g3.spot_guide.screens.profile.editProfile

import androidx.lifecycle.ViewModel
import com.g3.spot_guide.models.User

class EditProfileFragmentViewModel : ViewModel() {

    var userName: String = ""
    var fullName: String = ""
    var stance: String = ""
    var aboutMe: String = ""
    var sponsors: String = ""
    var instagramUrl: String = ""

    fun fillAttributesWithValues(user: User) {
        userName = user.nick
        fullName = "${user.firstName} ${user.lastName}"
        stance = user.stance
        aboutMe = user.aboutMe
        sponsors = user.sponsors
        instagramUrl = user.instagramUrl
    }
}