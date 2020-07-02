package com.g3.spot_guide.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import com.g3.spot_guide.R
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.MapFragmentBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.innfinity.permissionflow.lib.Permission
import com.innfinity.permissionflow.lib.permissionFlow
import com.innfinity.permissionflow.lib.withFragment
import com.innfinity.permissionflow.lib.withPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val DEFAULT_ZOOM_LEVEL = 17f


class MapFragment : BaseFragment<MapFragmentBinding, MapFragmentViewModel, MapFragmentHandler>(), GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener, MapFragmentHandler {

    override val viewModel: MapFragmentViewModel by viewModels { MapFragmentViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): MapFragmentBinding = MapFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: MapFragmentBinding, context: Context) {
        handlePermissions()
    }

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
                viewModel.lastKnownLocation = location
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
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

    private fun handlePermissions() {
        CoroutineScope(Dispatchers.Main).launch {
            permissionFlow {
                withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

                withFragment(this@MapFragment)

                requestEach().collect { permission: Permission ->
                    when (permission.permission) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            if (permission.isGranted) {
                                getAndSetLocation()
                            } else {
                                showSnackBar(binding.root, R.string.error__location_permission_denied_rationale, true)
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAndSetLocation() {
        googleMap?.isMyLocationEnabled = true

        val lastKnowLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnowLocation?.let { newLocation ->
            viewModel.lastKnownLocation = newLocation

            val latLng = LatLng(newLocation.latitude, newLocation.longitude)
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL))
        }

        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
    }

    override fun onMapLongClick(latLng: LatLng?) {
        // TODO
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        // TODO
        return true
    }
}

interface MapFragmentHandler : BaseFragmentHandler