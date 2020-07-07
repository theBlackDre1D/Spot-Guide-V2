package com.g3.spot_guide.repositories

import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.providers.SpotFirestoreProvider

class SpotRepository {

    private val provider: SpotFirestoreProvider by lazy { SpotFirestoreProvider() }

    suspend fun getAllSpots(): Either<List<Spot>> = provider.getAllSpots()

}