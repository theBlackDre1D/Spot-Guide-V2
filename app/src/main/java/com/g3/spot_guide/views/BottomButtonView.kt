package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.BottomButtonViewBinding
import com.g3.spot_guide.extensions.onClick

class BottomButtonView  : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = BottomButtonViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: BottomButtonViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(it) }
        }

    private fun setupView(configuration: BottomButtonViewConfiguration) {
        binding.buttonB.setText(configuration.buttonText)
        binding.buttonB.onClick { configuration.handler.onButtonClick() }
    }

    data class BottomButtonViewConfiguration(
        @StringRes val buttonText: Int,
        val handler: BottomButtonViewHandler
    )

    interface BottomButtonViewHandler {
        fun onButtonClick()
    }
}