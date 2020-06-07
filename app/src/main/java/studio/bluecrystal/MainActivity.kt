package studio.bluecrystal

import android.view.LayoutInflater
import studio.bluecrystal.base.BaseActivity
import studio.bluecrystal.base.BaseFragmentHandler
import studio.bluecrystal.databinding.MainActivityBinding

class MainActivity : BaseActivity<MainActivityBinding>(), BaseFragmentHandler {

    override fun setNavigationGraph() = R.id.homeNavigationContainer
    override fun setBinding(layoutInflater: LayoutInflater): MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: MainActivityBinding) {

    }
}