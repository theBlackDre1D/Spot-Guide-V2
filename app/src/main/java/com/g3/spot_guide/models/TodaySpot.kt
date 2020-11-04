package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel

data class TodaySpot(
    val spotId: String,
    val spotName: String,
    val date: String,
    val time: String
) : IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "spotId" to spotId,
            "spotName" to spotName,
            "date" to date,
            "time" to time
        )
    }
}