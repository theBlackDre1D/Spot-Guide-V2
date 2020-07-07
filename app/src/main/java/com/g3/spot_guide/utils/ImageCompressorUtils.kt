package com.g3.spot_guide.utils

import android.content.Context
import android.graphics.Bitmap
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File

private const val COMPRESSED_IMAGE_FILE_SIZE = 2_097_152

object ImageCompressorUtils {

    suspend fun compressImage(context: Context, imageFile: File): File {
        return Compressor.compress(context, imageFile) {
            resolution(1290, 720)
            quality(70)
            format(Bitmap.CompressFormat.JPEG)
            size(COMPRESSED_IMAGE_FILE_SIZE.toLong())
        }
    }

}