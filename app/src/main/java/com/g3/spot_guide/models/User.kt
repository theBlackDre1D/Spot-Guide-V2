package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel

data class User(
    val username: String = "",
    val email: String = "",
    val password: String = ""
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )
    }
}