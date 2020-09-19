package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.HeaderWithEditTextOutlinedViewBinding
import com.g3.spot_guide.extensions.afterTextChanged

class HeaderWithEditTextOutlinedView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = HeaderWithEditTextOutlinedViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: HeaderWithEditTextOutlinedViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(it) }
        }

    private fun setupView(configuration: HeaderWithEditTextOutlinedViewConfiguration) {
        binding.inputTIET.afterTextChanged { input ->
            configuration.handler.onInputTextChanged(input)
        }

        binding.headerTV.setText(configuration.headerText)
        binding.inputTIET.setText(configuration.inputText)
    }

    data class HeaderWithEditTextOutlinedViewConfiguration(
        @StringRes val headerText: Int,
        val inputText: String,
        val handler: HeaderWithEditTextOutlinedViewHandler
    )

    interface HeaderWithEditTextOutlinedViewHandler {
        fun onInputTextChanged(input: String)
    }
}