package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel
import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val nick: String = "",
    val email: String = "",
    val aboutMe: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val friends: List<String> = listOf(),
    val instagramNick: String? = null,
    val sponsors: String = "",
    val stance: String = "",
    val profilePictureUrl: String = "",
    val todaySpot: List<String>? = null,
    val memberRequests: List<String> = listOf()
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "nick" to nick,
            "email" to email,
            "aboutMe" to aboutMe,
            "firstName" to firstName,
            "lastName" to lastName,
            "friends" to friends,
            "instagramUrl" to instagramNick,
            "sponsors" to sponsors,
            "stance" to stance,
            "profilePictureUrl" to profilePictureUrl,
            "todaySpot" to todaySpot
        )
    }

    val fullName: String
        get() = "$firstName $lastName"

    val todaySpotObject: TodaySpot?
        get() {
            return if (todaySpot != null && todaySpot.size == 4) {
                val id = todaySpot[0]
                val name = todaySpot[1]
                val date = todaySpot[2]
                val time = todaySpot[3]
                TodaySpot(id, name, date, time)
            } else { null }
        }
}