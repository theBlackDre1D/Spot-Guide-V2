package com.g3.spot_guide.screens.onBoarding

import android.view.LayoutInflater
import com.g3.base.screens.activity.BaseActivity
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.OnBoardingActivityBinding
import com.pixplicity.easyprefs.library.Prefs

const val SHOW_ON_BOARDING__PREFS_KEY = "SHOW_ON_BOARDING__PREFS_KEY"

class OnBoardingActivity : BaseActivity<OnBoardingActivityBinding, Nothing>(), OnBoardingFragmentHandler {

    override fun setNavigationGraph() = R.id.onBoardingNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): OnBoardingActivityBinding = OnBoardingActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: OnBoardingActivityBinding) {}

    override fun onNextClick() {
        Prefs.putBoolean(SHOW_ON_BOARDING__PREFS_KEY, false)
        Session.application.coordinator.startMapActivity(this)
    }
}