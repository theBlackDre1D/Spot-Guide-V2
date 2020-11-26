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

    var configuration: EmojiRatingViewConfiguration? = null
        set(value) {
            field = value
            value?.let { setupView(value) }
        }

    fun setRating(rating: Int) {
        val correctImageView = when (rating) {
            1 -> binding.horribleIV
            2 -> binding.badIV
            3 -> binding.okIV
            4 -> binding.goodIV
            else -> binding.perfectIV
        }

        setupSelectedEmoji(correctImageView, SIZE_SELECTED)
    }

    private fun setupView(configuration: EmojiRatingViewConfiguration) {
        binding.horribleIV.onClick {
            onEmojiClick(GroundType.HORRIBLE, configuration.listener)
        }

        binding.badIV.onClick {
            onEmojiClick(GroundType.BAD, configuration.listener)
        }

        binding.okIV.onClick {
            onEmojiClick(GroundType.OK, configuration.listener)
        }

        binding.goodIV.onClick {
            onEmojiClick(GroundType.GOOD, configuration.listener)
        }

        binding.perfectIV.onClick {
            onEmojiClick(GroundType.PERFECT, configuration.listener)
        }

        configuration.groundType?.let { type ->
            onEmojiClick(type, configuration.listener)
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

    data class EmojiRatingViewConfiguration(
        val groundType: GroundType?,
        val listener: EmojiRatingViewListener
    )

    interface EmojiRatingViewListener{
        fun onEmojiClick(type: GroundType)
    }

}