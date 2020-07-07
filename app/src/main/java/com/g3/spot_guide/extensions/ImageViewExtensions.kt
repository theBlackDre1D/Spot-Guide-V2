package com.g3.spot_guide.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUri(uri: Uri?) {
    Glide
        .with(this)
        .load(uri).
        into(this)
}