package com.g3.spot_guide.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.g3.spot_guide.databinding.EmojiRatingViewBinding
import com.g3.spot_guide.enums.GroundType
import com.g3.spot_guide.extensions.dp
import com.g3.spot_guide.extensions.onClick

private const val SIZE_SELECTED = 40
private const val SIZE_UNSELECTED = 30

class EmojiRatingView : ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding = EmojiRatingViewBinding.inflate(LayoutInflater.from(context), this, true)

    var listener: EmojiRatingViewListener? = null
        set(value) {
            field = value
            value?.let {
                setupView(value)
            }
        }

    private fun setupView(listener: EmojiRatingViewListener) {
        binding.horribleIV.onClick {
            onEmojiClick(GroundType.HORRIBLE, listener)
        }

        binding.badIV.onClick {
            onEmojiClick(GroundType.BAD, listener)
        }

        binding.okIV.onClick {
            onEmojiClick(GroundType.OK, listener)
        }

        binding.goodIV.onClick {
            onEmojiClick(GroundType.GOOD, listener)
        }

        binding.perfectIV.onClick {
            onEmojiClick(GroundType.PERFECT, listener)
        }
    }

    private fun onEmojiClick(type: GroundType, listener: EmojiRatingViewListener) {
        listener.onEmojiClick(type)
        val emojies = listOf(binding.horribleIV, binding.badIV, binding.okIV, binding.goodIV, binding.perfectIV)
        emojies.forEachIndexed { index, imageView ->
            setupSelectedEmoji(imageView, SIZE_UNSELECTED)
        }

        when (type) {
            GroundType.HORRIBLE -> setupSelectedEmoji(binding.horribleIV, SIZE_SELECTED)
            GroundType.BAD -> setupSelectedEmoji(binding.badIV, SIZE_SELECTED)
            GroundType.OK -> setupSelectedEmoji(binding.okIV, SIZE_SELECTED)
            GroundType.GOOD -> setupSelectedEmoji(binding.goodIV, SIZE_SELECTED)
            GroundType.PERFECT -> setupSelectedEmoji(binding.perfectIV, SIZE_SELECTED)
        }

        binding.root.requestLayout()
    }

    private fun setupSelectedEmoji(imageView: ImageView, size: Int) {
        val layoutParams = imageView.layoutParams
        layoutParams.height = size.dp
        layoutParams.width = size.dp
    }

    interface EmojiRatingViewListener{
        fun onEmojiClick(type: GroundType)
    }

}