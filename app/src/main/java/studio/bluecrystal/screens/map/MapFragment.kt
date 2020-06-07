package studio.bluecrystal.screens.map

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
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import studio.bluecrystal.R
import studio.bluecrystal.base.BaseFragment
import studio.bluecrystal.base.BaseFragmentHandler
import studio.bluecrystal.databinding.HomeFragmentBinding
import java.util.*

private const val DEFAULT_ZOOM_LEVEL = 15F

class HomeFragment : BaseFragment<HomeFragmentBinding, HomeFragmentHandler>(), GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {

    private var mMapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private val locationManager: LocationManager? by lazy {
        context?.let {
            it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let { newLocation ->
                val newState = viewModel.state.value?.copy(lastKnowLocation = newLocation)
                viewModel.state.postValue(newState)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    private val viewModel: MapFragmentViewModel by viewModels { MapFragmentViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): HomeFragmentBinding = HomeFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: HomeFragmentBinding, context: Context) {
        handleLocationPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.initMap(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupViewModelStateObserver() {
        viewModel.state.observe(this, Observer {
            it?.let { state ->
                state.lastKnowLocation?.let { newLocation ->
                    val latLng = LatLng(newLocation.latitude, newLocation.longitude)
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            }
        })
    }

    private fun handleLocationPermission() {
        Permissions.check(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION, null,
            object : PermissionHandler() {
                override fun onGranted() {
                    getAndSetLocation()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    showSnackBar(binding.root, R.string.error__location_permission_denied_rationale, true)
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun getAndSetLocation() {
        val lastKnowLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnowLocation?.let { newLocation ->
            val newState = viewModel.state.value?.copy(lastKnowLocation = newLocation)
            viewModel.state.postValue(newState)
//
//            val latLng = LatLng(newLocation.latitude, newLocation.longitude)
//            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL))
        }

        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, locationListener)
    }

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

    override fun onMapLongClick(p0: LatLng?) {
        // TODO
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        // TODO
        return true
    }

}

interface HomeFragmentHandler : BaseFragmentHandler