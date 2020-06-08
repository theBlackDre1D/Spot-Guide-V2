package com.bluecrystal

import android.view.LayoutInflater
import com.bluecrystal.base.BaseActivity
import com.bluecrystal.base.BaseFragmentHandler
import com.bluecrystal.databinding.MainActivityBinding

class MainActivity : BaseActivity<MainActivityBinding>(), BaseFragmentHandler {

    override fun setNavigationGraph() = R.id.homeNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityBinding) {

    }
}