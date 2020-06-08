package com.bluecrystal.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<BINDING : ViewBinding, HANDLER : BaseFragmentHandler> : BottomSheetDialogFragment(), LifecycleObserver {

    protected lateinit var handler: HANDLER
    protected lateinit var binding: BINDING


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.handler = context as HANDLER
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = this.setBinding(inflater)
        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this@BaseBottomSheetDialogFragment.context?.let {
            this.onFragmentLoadingFinished(this@BaseBottomSheetDialogFragment.binding, it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun create() {
        this.onFragmentCreated()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        this.onFragmentStarted()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun resume() {
        this.onFragmentResumed()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun pause() {
        this.onFragmentPaused()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop() {
        this.onFragmentStopped()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        this.onFragmentDestroyed()
    }

    protected open fun onFragmentCreated() {}
    protected open fun onFragmentStarted() {}
    protected open fun onFragmentResumed() {}
    protected open fun onFragmentPaused() {}
    protected open fun onFragmentStopped() {}
    protected open fun onFragmentDestroyed() {}

    protected abstract fun setBinding(layoutInflater: LayoutInflater): BINDING
    protected abstract fun onFragmentLoadingFinished(binding: BINDING, context: Context)
}