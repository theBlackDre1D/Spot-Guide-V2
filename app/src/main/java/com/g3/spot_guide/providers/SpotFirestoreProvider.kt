package com.g3.spot_guide.providers

import com.g3.spot_guide.base.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.Spot
import kotlinx.coroutines.tasks.await

class SpotFirestoreProvider : BaseFirestoreProvider(FirestoreEntityName.SPOTS) {

    suspend fun getAllSpots(): Either<List<Spot>> {
        return try {
            val result = collectionReference.get().await()
            val spots = result.toObjects(Spot::class.java)
            result.documents.forEachIndexed { index, documentSnapshot ->
                spots[index].id = documentSnapshot.id
            }
            Either.Success(spots)
        } catch (e: Exception) { Either.Error() }
    }
}