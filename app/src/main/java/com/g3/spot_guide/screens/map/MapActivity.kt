package com.g3.spot_guide.screens.map

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.BaseActivity
import com.g3.spot_guide.databinding.MainActivityBinding
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.google.android.gms.maps.model.LatLng


class MapActivity : BaseActivity<MainActivityBinding, MapActivityViewModel, Nothing>(), MapFragmentHandler {

    override val viewModel: MapActivityViewModel by viewModels { MapActivityViewModel.ViewModelInstanceFactory(this) }
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
}