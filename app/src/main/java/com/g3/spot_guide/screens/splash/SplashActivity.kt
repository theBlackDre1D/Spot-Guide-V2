package com.g3.spot_guide.screens.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g3.spot_guide.Session
import com.g3.spot_guide.screens.onBoarding.SHOW_ON_BOARDING__PREFS_KEY
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity
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
                splashActivityViewModel.user.observe(this, { userEither ->
                    val user = userEither.getValueOrNull()
                    if (user != null) {
                        Session.saveAndSetLoggedInUser(user)

                        if (!user.isUserValid) {
                            val parameters = EditProfileActivity.Parameters(user)
                            Session.application.coordinator.startEditProfileActivity(this, parameters, true)
                        } else {
                            if (Prefs.getBoolean(SHOW_ON_BOARDING__PREFS_KEY, true)) {
                                Session.application.coordinator.startOnBoardingActivity(this)
                            } else {
                                Session.application.coordinator.startMapActivity(this)
                            }
                        }
                    } else {
                        Session.application.coordinator.startLoginActivity(this)
                    }
                })

                splashActivityViewModel.getUser(email)
            }
        } else {
            Session.application.coordinator.startLoginActivity(this)
        }
    }
}