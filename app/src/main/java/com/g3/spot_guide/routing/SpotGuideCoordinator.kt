package com.g3.spot_guide.routing

import android.app.Activity
import android.content.Intent
import com.g3.spot_guide.base.BaseCoordinator
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.map.MapActivity

class SpotGuideCoordinator : BaseCoordinator() {

    fun startMapActivity(activity: Activity) {
        val intent = Intent(activity, MapActivity::class.java)
        super.startActivity(activity, intent, true)
    }

    fun startAddSpotActivity(activity: Activity, parameters: AddSpotActivity.Parameters) {
        val intent = Intent(activity, AddSpotActivity::class.java)
        intent.putExtra(AddSpotActivity.ADD_SPOT_PARAMETERS__EXTRAS_KEY, parameters)
        super.startActivity(activity, intent)
    }
}