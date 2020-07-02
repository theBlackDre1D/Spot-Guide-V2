package com.g3.spot_guide.routing

import android.app.Activity
import android.content.Intent
import com.g3.spot_guide.MainActivity
import com.g3.spot_guide.base.BaseCoordinator

class SpotGuideCoordinator : BaseCoordinator() {

    fun startMainActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        super.startActivity(activity, intent, true)
    }
}