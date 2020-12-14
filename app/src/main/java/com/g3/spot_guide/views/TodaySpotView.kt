package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.TodaySpotViewBinding
import com.g3.spot_guide.extensions.onClick

class TodaySpotView  : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = TodaySpotViewBinding.inflate(LayoutInflater.from(context), this, true)

    var configuration: TodaySpotViewConfiguration? = null
        set(value) {
            field = value
            value?.let {
                setupView(it)
            }
        }

    private fun setupView(configuration: TodaySpotViewConfiguration) {
        binding.spotBubbleTV.text = configuration.spotName
        binding.root.onClick {
            configuration.handler.onBubbleClick()
        }
    }

    data class TodaySpotViewConfiguration(
        val spotName: String,
        val handler: TodaySpotViewHandler
    )

    interface TodaySpotViewHandler {
        fun onBubbleClick()
    }

}