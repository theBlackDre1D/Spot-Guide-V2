package com.g3.spot_guide.screens.addSpot

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.repositories.SpotRepository
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import java.io.File

@Suppress("UNCHECKED_CAST")
class AddSpotFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    private val repository: SpotRepository by lazy { SpotRepository() }

    var uploadSpotResult = MutableLiveData<Either<Unit>>()

    lateinit var locationData: LatLng
    var spotName: String? = null
    var spotRating: Int? = null
    var groundType: GroundType? = null
    var description: String? = null

    fun uploadSpot(context: Context, imageModels: List<ImageModel>) {
        doInCoroutine {
            val compressedImages = compressImage(context, imageModels)
            val uploadedImages = repository.uploadImages(compressedImages)
            if (uploadedImages is Either.Success) {
                val spot = Spot(
                    description = this.description ?: "",
                    latitude = this.locationData.latitude,
                    longitude = this.locationData.longitude,
                    name = this.spotName ?: "",
                    rating = this.spotRating ?: 0,
                    images = uploadedImages.value,
                    groundType = this.groundType?.typeName ?: ""
                )
                val result = repository.uploadSpot(spot)
                uploadSpotResult.postValue(result)
            }
        }
    }

    private suspend fun compressImage(context: Context, imageModels: List<ImageModel>): MutableList<File> {
        val images = mutableListOf<File>()
        imageModels.forEach { imageModel ->
            imageModel.path?.let {
                val imageFile = File(it)
                val deferred = CoroutineScope(Dispatchers.IO + SupervisorJob()).async {
                    Compressor.compress(context, imageFile) {
                        resolution(1280, 720)
                        quality(80)
                        format(Bitmap.CompressFormat.JPEG)
                        size(500000) // 2 MB
                    }
                }
                val compressedImage = deferred.await()
                images.add(compressedImage)
            }
        }
        return images
    }

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = AddSpotFragmentViewModel(handle, Session.application) as T
    }
}