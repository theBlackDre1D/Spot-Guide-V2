package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.FavoriteSpotViewBinding

class FavoriteSpotView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = FavoriteSpotViewBinding.inflate(LayoutInflater.from(context), this, true)

    var isFavorite: Boolean = false
        set(value) {
            field = value
            setupView(value)
        }

    private fun setupView(isFavorite: Boolean) {
        val drawableRes = if (isFavorite) R.drawable.ic_favorite_full else R.drawable.ic_favorite_empty
        binding.imageIV.setImageResource(drawableRes)
    }

}