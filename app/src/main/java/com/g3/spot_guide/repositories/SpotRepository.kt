package com.g3.spot_guide.repositories

import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.providers.SpotFirestoreProvider
import java.io.File

class SpotRepository {

    private val provider: SpotFirestoreProvider by lazy { SpotFirestoreProvider() }

    suspend fun getAllSpots() = provider.getAllSpots()
    suspend fun uploadImages(images: List<File>)= provider.uploadImages(images)
    suspend fun uploadSpot(spot: Spot) = provider.uploadSpot(spot)

}