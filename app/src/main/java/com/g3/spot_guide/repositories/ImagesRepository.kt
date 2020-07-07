package com.g3.spot_guide.repositories

import android.net.Uri
import android.provider.MediaStore
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.ImageModel
import java.util.*

class ImagesRepository {

    fun loadImages(): Either<List<ImageModel>> {
        val context = Session.application
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
            MediaStore.MediaColumns.DATE_ADDED + " DESC")
        val images = mutableListOf<ImageModel>()

        try {
            cursor?.let {
                it.moveToFirst()
                for (i in 0 until it.count) {
                    cursor.moveToPosition(i)
                    val columnIndexId = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val imageID = it.getInt(columnIndexId)
                    val imageData = it.getString(columnIndexData)
                    val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageID.toString())
                    val im = ImageModel(
                        UUID.randomUUID().toString(),
                        imageUri,
                        imageData,
                        null
                    )
                    images.add(im)
                }
            }
        } catch (e: Exception) {
            // Nothing yet
        }
        cursor?.close()
        return Either.Success(images)
    }
}