package com.g3.spot_guide.screens.login

import android.view.LayoutInflater
import com.g3.base.screens.activity.BaseActivity
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.LogInActivityBinding
import com.g3.spot_guide.extensions.navigateSafe

class LogInActivity : BaseActivity<LogInActivityBinding, Nothing>(), LoginFragmentHandler, RegisterFragmentHandler {

    override fun setNavigationGraph() = R.id.loginNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): LogInActivityBinding = LogInActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: LogInActivityBinding) {}

    override fun openMapScreen() {
        Session.application.coordinator.startMapActivity(this)
    }

    override fun openRegisterScreen() {
        navController?.navigateSafe(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    override fun navigateBack() = super.onBackPressed()
}