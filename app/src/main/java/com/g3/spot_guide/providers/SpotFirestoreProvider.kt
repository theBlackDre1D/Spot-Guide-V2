package com.g3.spot_guide.providers

import com.g3.spot_guide.base.Either
import com.g3.spot_guide.enums.FirestoreEntityName
import com.g3.spot_guide.models.Spot
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*

class SpotFirestoreProvider : BaseFirestoreProvider(FirestoreEntityName.SPOTS) {

    suspend fun getAllSpots(): Either<List<Spot>> {
        return try {
            val result = collectionReference.get().await()
            val spots = result.toObjects(Spot::class.java)
            result.documents.forEachIndexed { index, documentSnapshot ->
                spots[index].id = documentSnapshot.id
            }
            Either.Success(spots)
        } catch (e: Exception) {
            Either.Error(null)
        }
    }

    suspend fun uploadImages(images: List<File>): Either<List<String>> {
        return try {
            val storageRef = storage.reference
            val storageImagesReferences = mutableListOf<String>()
            images.forEach { imageFile ->
                val storageReferenceString = "spot_images/${UUID.randomUUID()}"
                val imageReference = storageRef.child(storageReferenceString)
                val uploadTask = imageReference.putBytes(imageFile.readBytes())
                uploadTask.await()
                storageImagesReferences.add(storageReferenceString)
            }
            Either.Success(storageImagesReferences)
        } catch (e: Exception) {
            Either.Error(e.message)
        }
    }

    suspend fun uploadSpot(spot: Spot): Either<Unit> {
        return try {
            db.collection(FirestoreEntityName.SPOTS.collectionName).document().set(spot.toUploadModel()).await()
            Either.Success(Unit)
        } catch (e: Exception) {
            Either.Error(e.message)
        }
    }
}