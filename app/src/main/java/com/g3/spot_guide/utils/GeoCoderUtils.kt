import android.content.Context
import android.location.Geocoder
import com.g3.spot_guide.R
import com.google.android.gms.maps.model.LatLng

object GeoCoderUtils {

    fun getNameFromLocation(context: Context, location: LatLng): String {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.first()
        return if (address != null) {
            "${address.locality ?: address.subLocality} ${address.premises}, ${address.countryCode}"
        } else context.getString(R.string.map__unknown_location)
    }
}