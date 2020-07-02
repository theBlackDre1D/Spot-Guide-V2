package com.g3.spot_guide

import android.view.LayoutInflater
import com.g3.spot_guide.base.BaseActivity
import com.g3.spot_guide.databinding.MainActivityBinding
import com.g3.spot_guide.screens.map.MapFragmentHandler

class MainActivity : BaseActivity<MainActivityBinding>(), MapFragmentHandler {

    override fun setNavigationGraph() = R.id.mainNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityBinding) {

    }
}