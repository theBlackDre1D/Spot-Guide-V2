package com.g3.spot_guide.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g3.spot_guide.Session
import com.g3.spot_guide.screens.onBoarding.SHOW_ON_BOARDING__PREFS_KEY
import com.google.firebase.auth.FirebaseAuth
import com.pixplicity.easyprefs.library.Prefs
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val splashActivityViewModel: SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val loggedUserEmail = FirebaseAuth.getInstance().currentUser?.email
            loggedUserEmail?.let { email ->
                splashActivityViewModel.user.observe(this, { user ->
                    Session.saveAndSetLoggedInUser(user)

                    if (Prefs.getBoolean(SHOW_ON_BOARDING__PREFS_KEY, true)) {
                        Session.application.coordinator.startOnBoardingActivity(this)
                    } else {
                        Session.application.coordinator.startMapActivity(this)
                    }
                })

                splashActivityViewModel.getUser(email)
            }
        } else {
            Session.application.coordinator.startLoginActivity(this)
        }
    }
}