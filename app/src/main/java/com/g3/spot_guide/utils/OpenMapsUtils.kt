package com.g3.spot_guide.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.g3.spot_guide.models.Spot

object OpenMapsUtils {

    fun openMapOnLocation(activity: Activity, spot: Spot) {
        val gmmIntentUri = Uri.parse("geo:${spot.latitude},${spot.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        activity.startActivity(mapIntent)
    }
}