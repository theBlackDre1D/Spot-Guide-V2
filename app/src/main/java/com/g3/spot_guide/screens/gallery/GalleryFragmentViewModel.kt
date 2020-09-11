package com.g3.spot_guide.screens.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.g3.base.either.Either
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.repositories.ImagesRepository
import kotlinx.coroutines.launch

class GalleryFragmentViewModel(
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    val images = MutableLiveData<Either<List<ImageModel>>>()
    val pickedImages = mutableListOf<ImageModel>()

    fun getImagesFromStorage() {
        viewModelScope.launch {
            images.postValue(imagesRepository.loadImages())
        }
    }
}