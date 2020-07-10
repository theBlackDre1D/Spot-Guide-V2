package com.g3.spot_guide.models

import com.g3.spot_guide.base.IHasUploadModel
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
    var groundType: String = ""
) : Serializable, IHasUploadModel {

    override fun toUploadModel(): HashMap<String, Any?> {
        return hashMapOf(
            "description" to description,
            "latitude" to latitude,
            "longitude" to longitude,
            "name" to name,
            "rating" to rating,
            "images" to images,
            "groundType" to groundType
        )
    }

    val location: LatLng
        get() {
            return LatLng(latitude.toDouble(), longitude.toDouble())
        }
}