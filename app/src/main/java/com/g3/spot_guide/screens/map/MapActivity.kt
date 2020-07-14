package com.g3.spot_guide.screens.map

import android.net.Uri
import android.view.LayoutInflater
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.BaseActivity
import com.g3.spot_guide.databinding.MainActivityBinding
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.google.android.gms.maps.model.LatLng


class MapActivity : BaseActivity<MainActivityBinding, Nothing>(), MapFragmentHandler, SpotDetailFragmentHandler {

    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityBinding) {}

    override fun openSpotDetailScreen(spot: Spot) {
        navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToSpotDetailFragment(spot))
    }

    override fun openAddSpotScreen(latLng: LatLng) {
        val params = AddSpotActivity.Parameters(latLng.latitude, latLng.longitude)
        Session.application.coordinator.startAddSpotActivity(this, params)
    }

    override fun openImagesGallery(images: List<Uri>, position: Int) {
        val params = ImagesPreviewFragmentArguments(images, position)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToImagesPreview(params))
    }
}