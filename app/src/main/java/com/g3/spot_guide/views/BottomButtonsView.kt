package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.BottomButtonsViewBinding
import com.g3.spot_guide.extensions.onClick

class BottomButtonsView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = BottomButtonsViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: BottomButtonsViewConfiguration? = null
        set(value) {
            field = value
            value?.let {
                setupView(value)
            }
        }

    private fun setupView(configuration: BottomButtonsViewConfiguration) {
        binding.leftB.setText(configuration.leftButtonText)
        binding.rightB.setText(configuration.rightButtonText)

        binding.leftB.onClick {
            configuration.listener.onLeftButtonClick()
        }

        binding.rightB.onClick {
            configuration.listener.onRightButtonClick()
        }
    }


    data class BottomButtonsViewConfiguration(
        @StringRes val leftButtonText: Int,
        @StringRes val rightButtonText: Int,
        val listener: BottomButtonsViewListener
    )

    interface BottomButtonsViewListener {
        fun onLeftButtonClick()
        fun onRightButtonClick()
    }
}