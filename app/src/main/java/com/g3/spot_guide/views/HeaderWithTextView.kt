package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.HeaderWithTextViewBinding
import com.g3.spot_guide.extensions.onClick

class HeaderWithTextView  : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = HeaderWithTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: HeaderWithTextViewConfiguration? = null
        set(value) {
            field = value
            value?.let {
                setupView(it)
            }
        }

    private fun setupView(configuration: HeaderWithTextViewConfiguration) {
        binding.headerTV.setText(configuration.headerText)
        binding.textTV.text = configuration.text

        configuration.textColor?.let { color ->
            binding.textTV.setTextColor(color)
        }

        binding.textTV.onClick {
            configuration.handler?.onTextClicked()
        }
    }

    data class HeaderWithTextViewConfiguration(
        @StringRes val headerText: Int,
        val text: String,
        @ColorRes val textColor: Int? = null,
        val handler: HeaderWithTextViewHandler? = null
    )

    interface HeaderWithTextViewHandler {
        fun onTextClicked()
    }
}