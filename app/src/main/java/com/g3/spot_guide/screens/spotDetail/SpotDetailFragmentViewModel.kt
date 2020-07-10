package com.g3.spot_guide.screens.spotDetail

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.repositories.SpotRepository

class SpotDetailFragmentViewModel(
    private val repository: SpotRepository
) : ViewModel() {

    val imagesUris = MutableLiveData<MutableList<Uri>>(mutableListOf())

    fun loadImages(imagesPaths: List<String>) {
        doInCoroutine {
            imagesPaths.forEach { imagePath ->
                val result = repository.downloadImage(imagePath)
                if (result is Either.Success) {
                    val currentImages = imagesUris.value
                    currentImages?.add(result.value)

                }
            }
        }
    }
}