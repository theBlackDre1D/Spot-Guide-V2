package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel

data class SpotReview(
    val spotId: String = "",
    val date: String = "",
    val spotRating: Int = 1,
    val text: String = "",
    val userId: String = ""
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "spotId" to spotId,
            "date" to date,
            "spotRating" to spotRating,
            "text" to text,
            "userId" to userId
        )
    }

    var user: User? = null
}