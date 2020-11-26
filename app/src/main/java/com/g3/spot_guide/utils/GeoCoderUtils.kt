import android.content.Context
import android.location.Geocoder
import com.g3.spot_guide.R
import com.google.android.gms.maps.model.LatLng

object GeoCoderUtils {

    fun getNameFromLocation(context: Context, location: LatLng): String {
        return try {
            val geocoder = Geocoder(context)
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.first()
            if (address != null) {
                val result = "${address.locality ?: address.subLocality} ${address.premises}, ${address.countryCode}"
                result.replace("null", "")
            } else context.getString(R.string.map__unknown_location)
        } catch (e: Exception) {
            context.getString(R.string.map__unknown_location)
        }
    }
}