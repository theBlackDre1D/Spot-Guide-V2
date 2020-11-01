package com.g3.base.screens.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.g3.base.R
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseBottomSheet<BINDING: ViewBinding, HANDLER: BaseFragmentHandler> : BottomSheetDialogFragment(), LifecycleObserver {

    protected lateinit var binding: BINDING
    protected lateinit var handler: HANDLER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this)
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        this.binding = this.setBinding(inflater)
        return this.binding.root
    }

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.handler = context as HANDLER
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this@BaseBottomSheet.context?.let {
            this.onFragmentLoadingFinished(this@BaseBottomSheet.binding, it)
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

    protected fun showSnackBar(rootView: View, @StringRes text: Int, longDuration: Boolean = false) {
        Snackbar.make(rootView, text, if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
    }

    protected fun showSnackBar(rootView: View, text: String?, longDuration: Boolean = false) {
        text?.let {
            Snackbar.make(rootView, text, if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).show()
        }
    }

    protected abstract fun setBinding(layoutInflater: LayoutInflater): BINDING
    protected abstract fun onFragmentLoadingFinished(binding: BINDING, context: Context)

    protected open fun onFragmentCreated() {}
    protected open fun onFragmentStarted() {}
    protected open fun onFragmentResumed() {}
    protected open fun onFragmentPaused() {}
    protected open fun onFragmentStopped() {}
    protected open fun onFragmentDestroyed() {}
}