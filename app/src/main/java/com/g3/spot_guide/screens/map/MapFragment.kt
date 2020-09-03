package com.g3.spot_guide.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.araujo.jordan.excuseme.ExcuseMe
import com.g3.spot_guide.R
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.databinding.MapFragmentBinding
import com.g3.spot_guide.enums.SpotType
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.views.AppBarView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

private const val DEFAULT_ZOOM_LEVEL = 17f


class MapFragment : BaseFragment<MapFragmentBinding, MapFragmentHandler>(), GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private val locationManager: LocationManager? by lazy {
        context?.let {
            it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                mapFragmentViewModel.lastKnownLocation = location
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    private val mapFragmentViewModel: MapFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): MapFragmentBinding = MapFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: MapFragmentBinding, context: Context) {
        setupLoading()
        handlePermissions()
        setupObservers()
        setupButtonsListeners()
        setupAppBarView()
    }

    override fun onFragmentResumed() {
        super.onFragmentResumed()
        // TODO Thing about better implementation
//        mapFragmentViewModel.mostRecentOpenedSpot?.let { handler.openSpotDetailScreen(it) }
        mapFragmentViewModel.getAllSpots()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.initMap(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    private fun initMap(savedInstanceState: Bundle?) {
        mMapView = binding.mapView
        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume() // needed to common.get the common.map to display immediately
        MapsInitializer.initialize(activity?.applicationContext)

        mMapView?.getMapAsync { map ->
            googleMap = map
            googleMap?.setOnMapLongClickListener(this)
            googleMap?.setOnMarkerClickListener(this)
        }
    }

    private fun setupButtonsListeners() {
        binding.addSpotB.onClick {
            mapFragmentViewModel.lastKnownLocation?.let { location ->
                val latLng = LatLng(location.latitude, location.longitude)
                handler.openAddSpotScreen(latLng)
            }
        }

        binding.filterSpotsFAB.onClick { handler.openSpotsFilterSheet() }
    }

    private fun setupLoading() {
        binding.loadingV.isBlurVisible = false
    }

    private fun setupObservers() {
        mapFragmentViewModel.spots.observe(this, Observer { spots ->
            when (spots) {
                is Either.Error -> showSnackBar(binding.root, R.string.error__spots_load)
                is Either.Success -> spots.value.forEach { spot -> createMarkerAtLocation(spot) }
            }
            binding.loadingV.isVisible = false
        })

        val filtersLiveData = handler.getFiltersLiveData()
        filtersLiveData.observe(this, Observer { filters ->
            val loadedSpots = mapFragmentViewModel.spots.value
            if (loadedSpots is Either.Success) {
                googleMap?.clear() // delete all markers
                loadedSpots.value.forEach { spot ->
                    if (filters.isEmpty()) createMarkerAtLocation(spot)
                    else {
                        if (filters.contains(spot.spotTypeEnum)) createMarkerAtLocation(spot)
                    }
                }
            }
        })
    }

    private fun createMarkerAtLocation(spot: Spot) {
        val marker = MarkerOptions().position(spot.location)
        val pinIcon = bitmapDescriptorFromVector(R.drawable.ic_pin)
        marker.icon(pinIcon)
        val addedMarker = googleMap?.addMarker(marker)
        addedMarker?.tag = spot
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(requireContext(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun handlePermissions() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = ExcuseMe.couldYouGive(requireContext()).permissionFor(Manifest.permission.ACCESS_FINE_LOCATION)
            if (result) {
                getAndSetLocation()
            } else {
                showSnackBar(binding.root, R.string.error__location_permission_denied_rationale)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAndSetLocation() {
        googleMap?.isMyLocationEnabled = true

        val lastKnowLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnowLocation?.let { newLocation ->
            mapFragmentViewModel.lastKnownLocation = newLocation

            val latLng = LatLng(newLocation.latitude, newLocation.longitude)
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL))
        }

        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
    }

    private fun setupAppBarView() {
        val appBarViewHandler = object : AppBarView.AppBarViewHandler {
            override fun onLeftIconClick() {
                handler.getDrawer().open()
            }
        }

        val configuration = AppBarView.AppBarViewConfiguration(R.string.app_name, false, false, R.drawable.ic_drawer, appBarViewHandler)
        binding.appBarV.configuration = configuration
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let { handler.openAddSpotScreen(latLng) }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let {
            val spot = marker.tag as? Spot
            spot?.let {
                mapFragmentViewModel.mostRecentOpenedSpot = spot
                handler.openSpotDetailScreen(spot)
            }
            return true
        }
        return false
    }
}

interface MapFragmentHandler : BaseFragmentHandler {
    fun openSpotDetailScreen(spot: Spot)
    fun openAddSpotScreen(latLng: LatLng)
    fun openSpotsFilterSheet()
    fun getFiltersLiveData(): MutableLiveData<MutableList<SpotType>>
    fun getDrawer(): DrawerLayout
}