package com.g3.spot_guide.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object InstagramUtils {

    fun openInstagramProfile(context: Context, instagramNick: String) {
        val uriString = "http://instagram.com/_u/$instagramNick"
        val uri = Uri.parse(uriString)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
        }
    }
}