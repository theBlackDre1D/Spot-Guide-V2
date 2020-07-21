package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.TwoColorsTextViewBinding

class TwoColorsTextView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = TwoColorsTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: TwoColorsTextViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(it) }
        }

    private fun setupView(configuration: TwoColorsTextViewConfiguration) {
        binding.grayTV.setText(configuration.greyText)
        binding.purpleTV.setText(configuration.purpleText)
    }

    data class TwoColorsTextViewConfiguration(
        @StringRes val greyText: Int,
        @StringRes val purpleText: Int
    )
}