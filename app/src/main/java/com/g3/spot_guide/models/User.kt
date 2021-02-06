package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    @DocumentId val id: String = "",
    val nick: String = "",
    val email: String = "",
    val aboutMe: String = "",
    val fullName: String = "",
    val friends: List<String> = listOf(),
    val instagramNick: String? = null,
    val sponsors: String = "",
    val stance: String = "",
    val profilePictureUrl: String = "",
    val memberRequests: List<String> = listOf(),
    val todaySpot: TodaySpot? = null
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "nick" to nick,
            "email" to email,
            "aboutMe" to aboutMe,
            "fullName" to fullName,
            "friends" to friends,
            "instagramUrl" to instagramNick,
            "sponsors" to sponsors,
            "stance" to stance,
            "profilePictureUrl" to profilePictureUrl,
            "todaySpot" to todaySpot
        )
    }

    val isUserValid: Boolean
        get() {
            return nick != "" && aboutMe != "" && fullName != "" && profilePictureUrl != ""
        }
}