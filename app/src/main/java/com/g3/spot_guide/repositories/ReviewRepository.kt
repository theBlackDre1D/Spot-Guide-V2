package com.g3.spot_guide.repositories

import com.g3.spot_guide.models.SpotReview
import com.g3.spot_guide.providers.ReviewFirestoreProvider

class ReviewRepository(
    private val provider: ReviewFirestoreProvider
) {

    suspend fun getReviewsForSpot(spotId: String) = provider.getReviewsForSpot(spotId)
    suspend fun addReview(spotReview: SpotReview) = provider.addReview(spotReview)
}