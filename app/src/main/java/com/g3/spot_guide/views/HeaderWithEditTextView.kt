package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.HeaderWithEditTextViewBinding
import com.g3.spot_guide.extensions.afterTextChanged

class HeaderWithEditTextView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val binding = HeaderWithEditTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: HeaderWithEditTextViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(it) }
        }

    private fun setupView(configuration: HeaderWithEditTextViewConfiguration) {
        binding.inputET.afterTextChanged { input ->
            configuration.handler.onInputTextChanged(input)
        }

        binding.headerTV.setText(configuration.headerText)
        binding.inputET.setText(configuration.inputText)
    }

    data class HeaderWithEditTextViewConfiguration(
        @StringRes val headerText: Int,
        val inputText: String,
        val handler: HeaderWithEditTextViewHandler
    )

    interface HeaderWithEditTextViewHandler {
        fun onInputTextChanged(input: String)
    }
}