package com.g3.spot_guide.utils

import android.content.Context
import android.graphics.Bitmap
import com.g3.spot_guide.extensions.getFileName
import com.g3.spot_guide.models.ImageModel
import com.google.android.gms.common.util.IOUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val COMPRESSED_IMAGE_FILE_SIZE = 2_097_152

object ImageCompressorUtils {

    suspend fun compressImage(context: Context, imageModel: ImageModel): File? {
        val cachedImage = copyImageToCacheDir(context, imageModel)
        cachedImage?.let { image ->
            return Compressor.compress(context, cachedImage) {
                resolution(1290, 720)
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
                size(COMPRESSED_IMAGE_FILE_SIZE.toLong())
            }
        }

        return null
    }

    private fun copyImageToCacheDir(context: Context, imageModel: ImageModel): File? {
        imageModel.uri?.let { uri ->
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))

            parcelFileDescriptor?.let {
                val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val outputStream = FileOutputStream(file)
                IOUtils.copyStream(inputStream, outputStream)
            }

            return file
        }
        return null
    }
}