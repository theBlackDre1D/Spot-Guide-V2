package com.g3.spot_guide.screens.map

import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.BaseActivity
import com.g3.spot_guide.databinding.MainActivityBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.spotDetail.ImagesPreviewFragmentArguments
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentDirections
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentHandler
import com.google.android.gms.maps.model.LatLng
import org.koin.android.viewmodel.ext.android.viewModel

private const val DRAWER_GRAVITY = GravityCompat.START

class MapActivity : BaseActivity<MainActivityBinding, Nothing>(), MapFragmentHandler, SpotDetailFragmentHandler, FilterSpotsBottomSheetHandler {

    private val mapActivityViewModel: MapActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityBinding) {
        setupDrawer()
        setupNavigationView()
    }

    override fun openSpotDetailScreen(spot: Spot) {
        navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToSpotDetailFragment(spot))
    }

    override fun openAddSpotScreen(latLng: LatLng) {
        val params = AddSpotActivity.Parameters(latLng.latitude, latLng.longitude)
        Session.application.coordinator.startAddSpotActivity(this, params)
    }

    override fun openSpotsFilterSheet() {
        val arguments = FilterSpotsBottomSheetArguments(mapActivityViewModel.spotsFilters.value ?: listOf())
        navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToFilterSpotsBottomSheet(arguments))
    }

    override fun getFiltersLiveData(): MutableLiveData<MutableList<SpotType>> = mapActivityViewModel.spotsFilters

    override fun getDrawer() = binding.drawerLayout

    override fun openImagesGallery(images: List<Uri>, position: Int) {
        val params = ImagesPreviewFragmentArguments(images, position)
        navController?.navigateSafe(SpotDetailFragmentDirections.actionSpotDetailToImagesPreview(params))
    }

    override fun onSpotTypeCLick(spotType: SpotType) {
        val currentFiltersValue = mapActivityViewModel.spotsFilters.value
        if (currentFiltersValue != null) {
            if (currentFiltersValue.contains(spotType)) {
                currentFiltersValue.remove(spotType)
            } else {
                currentFiltersValue.add(spotType)
            }
        }
        mapActivityViewModel.spotsFilters.postValue(currentFiltersValue)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer__open, R.string.drawer__close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigationView() {
       binding.navigationView.setNavigationItemSelectedListener { menuItem ->
           when (menuItem.itemId) {
               R.id.menuProfile -> {
                   navController?.navigateSafe(MapFragmentDirections.actionMapFragmentToProfileFragment())
               }
               R.id.menuSettings -> {}
               R.id.menuLogOut -> {}
           }
           binding.drawerLayout.closeDrawer(DRAWER_GRAVITY)
           true
       }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            binding.drawerLayout.closeDrawer(DRAWER_GRAVITY)
        } else {
            super.onBackPressed()
        }
    }
}