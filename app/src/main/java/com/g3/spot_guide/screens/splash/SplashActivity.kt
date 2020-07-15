package com.g3.spot_guide.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g3.spot_guide.Session
import com.g3.spot_guide.screens.onBoarding.SHOW_ON_BOARDING__PREFS_KEY
import com.pixplicity.easyprefs.library.Prefs

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Prefs.getBoolean(SHOW_ON_BOARDING__PREFS_KEY, true)) {
            Session.application.coordinator.startOnBoardingActivity(this)
        } else {
            Session.application.coordinator.startMapActivity(this)
        }
    }
}