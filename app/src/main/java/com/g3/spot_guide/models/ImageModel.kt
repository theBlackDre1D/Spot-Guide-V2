package com.g3.spot_guide.models

import android.graphics.Bitmap
import android.net.Uri

data class ImageModel(
    val id: String?,
    val uri: Uri?,
    val path: String?,
    var bitmap: Bitmap?
)