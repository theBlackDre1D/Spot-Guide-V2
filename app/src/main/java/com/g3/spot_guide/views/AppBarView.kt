package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.g3.spot_guide.databinding.AppBarViewBinding
import com.g3.spot_guide.extensions.onClick

class AppBarView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = AppBarViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: AppBarViewConfiguration? = null
        set(value) {
            field = value
            value?.let {
                setupView(value)
            }
        }

    private fun setupView(configuration: AppBarViewConfiguration) {
        binding.middleTV.setText(configuration.middleText)
        binding.leftIconIV.isVisible = false

        binding.leftIconIV.onClick {
            configuration.handler?.onLeftIconClick()
        }

        configuration.leftIcon?.let { drawableResId ->
            binding.leftIconIV.setImageResource(drawableResId)
        }
    }

    fun showLoading(show: Boolean) {
        binding.loadingV.isVisible = show
    }

    data class AppBarViewConfiguration(
        @StringRes val middleText: Int,
        val backArrowVisible: Boolean,
        @DrawableRes val leftIcon: Int?,
        val handler: AppBarViewHandler?
    )

    interface AppBarViewHandler{
        fun onLeftIconClick()
    }
}