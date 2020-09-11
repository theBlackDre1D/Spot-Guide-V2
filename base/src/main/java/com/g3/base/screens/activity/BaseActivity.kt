package com.g3.base.screens.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<BINDING: ViewBinding, PARAMETERS : BaseParameters> : AppCompatActivity(),
    LifecycleObserver {

    protected lateinit var binding: BINDING
    protected var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this)

        this.binding = this.setBinding(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.initializeParameters()

        this.setNavigationGraph().let { navigationGraph ->
            this.navController = Navigation.findNavController(this, navigationGraph)
        }

        this.onActivityLoadingFinished(this.binding)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun create() {
        this.onActivityCreated()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        this.onActivityStarted()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        this.onActivityResumed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        this.onActivityPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop() {
        this.onActivityStopped()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        this.onActivityDestroyed()
    }

    private fun initializeParameters() = this.intent.extras?.let { extras -> this.createParameters()?.loadParameters(extras) }

    protected abstract fun setNavigationGraph(): Int
    protected abstract fun setBinding(layoutInflater: LayoutInflater): BINDING
    protected abstract fun onActivityLoadingFinished(binding: BINDING)

    protected open fun createParameters(): PARAMETERS? = null

    protected open fun onActivityCreated() {}
    protected open fun onActivityStarted() {}
    protected open fun onActivityResumed() {}
    protected open fun onActivityPaused() {}
    protected open fun onActivityStopped() {}
    protected open fun onActivityDestroyed() {}
}