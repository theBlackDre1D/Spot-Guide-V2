package com.g3.spot_guide.screens.login

import android.view.LayoutInflater
import com.g3.base.screens.activity.BaseActivity
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.LogInActivityBinding
import com.g3.spot_guide.extensions.navigateSafe
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity

class LogInActivity : BaseActivity<LogInActivityBinding, Nothing>(), LoginFragmentHandler, RegisterFragmentHandler {

    private var isLoggingInProgress = false

    override fun setNavigationGraph() = R.id.loginNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): LogInActivityBinding = LogInActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: LogInActivityBinding) {}

    override fun openMapScreen() {
        Session.application.coordinator.startMapActivity(this)
    }

    override fun openRegisterScreen() {
        navController?.navigateSafe(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    override fun setLoginProgress(isLoggingIn: Boolean) {
        isLoggingInProgress = isLoggingIn
    }

    override fun onBackPressed() {
        if (!isLoggingInProgress) {
            super.onBackPressed()
        }
    }

    override fun navigateBack() = onBackPressed()

    override fun fromRegisterToEditProfile(user: User) {
        val parameters = EditProfileActivity.Parameters(user)
        Session.application.coordinator.startEditProfileActivity(this, parameters, true)
    }
}