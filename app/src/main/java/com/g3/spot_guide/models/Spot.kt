package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel
import com.g3.spot_guide.enums.SpotType
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Spot(
    var id: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val rating: Int = 1,
    val images: List<String> = listOf(),
    val groundType: String = "",
    val spotType: String = ""
) : Serializable, IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "description" to description,
            "latitude" to latitude,
            "longitude" to longitude,
            "name" to name,
            "rating" to rating,
            "images" to images,
            "groundType" to groundType,
            "spotType" to spotType
        )
    }

    val location: LatLng get() = LatLng(latitude, longitude)

    val spotTypeEnum: SpotType
        get() {
            return when (spotType) {
                SpotType.SKATEPARK.spotName -> SpotType.SKATEPARK
                SpotType.GAP.spotName -> SpotType.GAP
                SpotType.BOX.spotName -> SpotType.BOX
                SpotType.STAIRS.spotName -> SpotType.STAIRS
                SpotType.HANDRAIL.spotName -> SpotType.HANDRAIL
                SpotType.DIY.spotName -> SpotType.DIY
                else -> SpotType.OTHER
            }
        }
}