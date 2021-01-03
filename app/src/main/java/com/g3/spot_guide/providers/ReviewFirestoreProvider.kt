package com.g3.spot_guide.providers

import com.g3.base.either.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.SpotReview
import kotlinx.coroutines.tasks.await

class ReviewFirestoreProvider : BaseFirestoreProvider(FirestoreEntityName.REVIEWS) {

    suspend fun getReviewsForSpot(spotId: String): Either<List<SpotReview>> {
        return try {
            val result = collectionReference.whereEqualTo("spotId", spotId).get().await()
            val reviews = result.toObjects(SpotReview::class.java)
            Either.Success(reviews)
        } catch (e: Exception) {
            Either.Error(e)
        }
    }

    suspend fun addReview(spotReview: SpotReview): Either<SpotReview> {
        return try {
            collectionReference.document().set(spotReview.toUploadModel()).await()
            Either.Success(spotReview)
        } catch (e: Exception) {
            Either.Error(null)
        }
    }
}