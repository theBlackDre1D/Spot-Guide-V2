package com.g3.spot_guide.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g3.spot_guide.Session

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Session.application.coordinator.startMainActivity(this)
    }
}