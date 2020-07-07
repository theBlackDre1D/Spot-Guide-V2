package com.g3.spot_guide.models

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Spot(
    var id: String = "",
    val description: String = "",
    val groundRating: Int = 0,
    val latitude: Long = 0,
    val longitude: Long = 0,
    val name: String = "",
    val rating: Int = 1
) : Serializable {

    val location: LatLng
        get() {
            return LatLng(latitude.toDouble(), longitude.toDouble())
        }
}