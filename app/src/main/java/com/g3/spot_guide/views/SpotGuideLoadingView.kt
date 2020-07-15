package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.g3.spot_guide.databinding.SpotGuideLoadingViewBinding

class SpotGuideLoadingView  : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = SpotGuideLoadingViewBinding.inflate(LayoutInflater.from(context), this, true)

    var isBlurVisible: Boolean = true
        set(value) {
            field = value
            binding.blurV.isVisible = value
        }
}