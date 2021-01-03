package com.g3.spot_guide.screens.spotReview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.g3.base.either.Either
import com.g3.spot_guide.Session
import com.g3.spot_guide.extensions.doInCoroutine
import com.g3.spot_guide.models.SpotReview
import com.g3.spot_guide.providers.ReviewFirestoreProvider
import com.g3.spot_guide.utils.DateUtils

class AddSpotReviewViewModel(
    private val reviewsProvider: ReviewFirestoreProvider
) : ViewModel() {

    val spotRating = MutableLiveData<Float>()
    val reviewText = MutableLiveData<String>()

    val addSpotReviewResult = MutableLiveData<Either<SpotReview>>()

    fun addReview(spotId: String) {
        doInCoroutine {
            spotRating.value?.let { rating ->
                reviewText.value?.let { text ->
                    Session.loggedInUser?.let { user ->
                        val spotReview = SpotReview(
                            spotId,
                            DateUtils.getTodayDate(),
                            rating.toInt(),
                            text,
                            user.id
                        )
                        val result = reviewsProvider.addReview(spotReview)
                        addSpotReviewResult.postValue(result)
                    }
                }
            }
        }
    }
}