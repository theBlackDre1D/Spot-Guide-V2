package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel

data class TodaySpot(
    val spotID: String? = null,
    val spotName: String,
    val date: String
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "spotID" to spotID,
            "spotName" to spotName,
            "date" to date
        )
    }
}