package com.g3.spot_guide.screens.spotDetail

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.repositories.SpotRepository
import com.g3.spot_guide.repositories.UserRepository


class SpotDetailFragmentViewModel(
    private val spotRepository: SpotRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val imagesUris = MutableLiveData<MutableList<Uri>>(mutableListOf())
    val spot = MutableLiveData<Either<Spot>>()
    val spotCrewMembers = MutableLiveData<Either<List<User>>>()

    fun loadImages(imagesPaths: List<String>) {
        doInCoroutine {
            imagesPaths.forEach { imagePath ->
                val result = spotRepository.downloadImage(imagePath)
                if (result is Either.Success) {
                    val currentImages = imagesUris.value
                    currentImages?.add(result.value)
                    imagesUris.postValue(currentImages)
                }
            }
        }
    }

    fun getSpot(spotId: String) {
        doInCoroutine {
            val spotResult = spotRepository.getSpotById(spotId)
            spot.postValue(spotResult)
        }
    }

    fun getCrewMembersForThisSpot(spotId: String) {
        doInCoroutine {
            val result = userRepository.getCrewMembersForThisSpot(spotId)
            spotCrewMembers.postValue(result)
        }
    }
}