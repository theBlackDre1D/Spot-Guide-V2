package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.SliderRatingViewBinding
import com.g3.spot_guide.enums.GroundType


class SliderRatingView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = SliderRatingViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: SliderRatingViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(value) }
        }

    private fun setupView(configuration: SliderRatingViewConfiguration) {
        binding.slider.progress = configuration.groundType?.ordinal ?: 0
        binding.slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    configuration.listener.onSliderValueChanged(progress)
                    setupProgressText(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        setupProgressText(0)
    }

    private fun setupProgressText(progress: Int) {
        binding.progressTextTV.text = when (progress) {
            GroundType.HORRIBLE.ordinal -> GroundType.HORRIBLE.typeName.toLowerCase()
            GroundType.BAD.ordinal -> GroundType.BAD.typeName.toLowerCase()
            GroundType.OK.ordinal -> GroundType.OK.typeName.toLowerCase()
            GroundType.GOOD.ordinal -> GroundType.GOOD.typeName.toLowerCase()
            else -> GroundType.PERFECT.typeName.toLowerCase()
        }
    }

    data class SliderRatingViewConfiguration(
        val groundType: GroundType?,
        val listener: SliderRatingViewListener
    )

    interface SliderRatingViewListener{
        fun onSliderValueChanged(newValue: Int)
    }

}