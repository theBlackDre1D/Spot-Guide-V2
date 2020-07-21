package com.g3.spot_guide.screens.login

import android.view.LayoutInflater
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.BaseActivity
import com.g3.spot_guide.databinding.LogInActivityBinding

class LogInActivity : BaseActivity<LogInActivityBinding, Nothing>(), LoginFragmentHandler {

    override fun setNavigationGraph() = R.id.loginNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): LogInActivityBinding = LogInActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: LogInActivityBinding) {}

    override fun openMapScreen() {
        Session.application.coordinator.startMapActivity(this)
    }
}