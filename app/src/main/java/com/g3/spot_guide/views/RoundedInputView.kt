package com.g3.spot_guide.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.RoundedInputViewBinding
import com.g3.spot_guide.extensions.afterTextChanged

class RoundedInputView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = RoundedInputViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: RoundedInputViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(it) }
        }

    private fun setupView(configuration: RoundedInputViewConfiguration) {
        binding.textTV.setText(configuration.titleText)
        binding.inputET.setText(configuration.inputText)

        if (configuration.secureInput) {
            binding.inputET.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        binding.inputET.afterTextChanged { configuration.listener.onTextChanged(it) }
    }

    data class RoundedInputViewConfiguration(
        @StringRes val titleText: Int,
        val inputText: String,
        val secureInput: Boolean,
        val listener: RoundedInputViewListener
    )

    interface RoundedInputViewListener {
        fun onTextChanged(text: String)
    }
}