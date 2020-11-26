package com.g3.spot_guide.screens.addSpot

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.extensions.getFileName
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.repositories.SpotRepository
import com.google.android.gms.common.util.IOUtils
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddSpotFragmentViewModel(
    private val repository: SpotRepository
) : ViewModel() {

    var uploadSpotResult = MutableLiveData<Either<Unit>>()

    lateinit var locationData: LatLng
    var spotName: String? = null
    var spotType: SpotType? = null
    var spotRating: Int? = null
    var groundType: GroundType? = null
    var description: String? = null

    fun uploadSpot(context: Context, imageModels: List<ImageModel>) {
        doInCoroutine {
            val compressedImages = compressImages(context, imageModels)
            val uploadedImages = repository.uploadImages(compressedImages)
            if (uploadedImages is Either.Success) {
                val spot = Spot(
                    description = this.description ?: "",
                    latitude = this.locationData.latitude,
                    longitude = this.locationData.longitude,
                    name = this.spotName ?: "",
                    rating = this.spotRating ?: 0,
                    images = uploadedImages.value,
                    groundType = this.groundType?.typeName ?: "",
                    spotType = this.spotType?.spotName ?: SpotType.OTHER.spotName
                )
                val result = repository.uploadSpot(spot)
                uploadSpotResult.postValue(result)
            }
        }
    }

    private suspend fun compressImages(context: Context, imageModels: List<ImageModel>): MutableList<File> {
        val images = mutableListOf<File>()
        val cachedImages = copyImagesToCacheDir(context, imageModels)
        cachedImages.forEach { imageFile ->
            val compressedImage = Compressor.compress(context, imageFile) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(500000) // 2 MB
                }
            images.add(compressedImage)
        }
        return images
    }

    private fun copyImagesToCacheDir(context: Context, imageModels: List<ImageModel>): MutableList<File> {
        val cachedImages = mutableListOf<File>()
        imageModels.forEach { imageModel ->
            imageModel.uri?.let { uri ->
                val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)

                parcelFileDescriptor?.let {
                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
                    val outputStream = FileOutputStream(file)
                    IOUtils.copyStream(inputStream, outputStream)
                    cachedImages.add(file)
                }
            }
        }

        return cachedImages
    }
}