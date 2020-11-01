package com.g3.spot_guide.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.BaseConfirmDialogFragmentBinding

open class BaseConfirmDialogFragment : DialogFragment() {

    protected lateinit var binding: BaseConfirmDialogFragmentBinding
    protected lateinit var handler: BaseConfirmDialogFragmentHandler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        this.binding = this.setBinding(inflater)
        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handler = context as BaseConfirmDialogFragmentHandler
        context?.let { nonNullContext ->
            onFragmentLoadingFinished(binding, nonNullContext)
        }
    }

    open fun setBinding(layoutInflater: LayoutInflater): BaseConfirmDialogFragmentBinding = BaseConfirmDialogFragmentBinding.inflate(layoutInflater)
    open fun onFragmentLoadingFinished(binding: BaseConfirmDialogFragmentBinding, context: Context) {

    }
}

interface BaseConfirmDialogFragmentHandler : BaseFragmentHandler {
    fun onPositiveButtonClick()
    fun onNegativeButtonClick()
}