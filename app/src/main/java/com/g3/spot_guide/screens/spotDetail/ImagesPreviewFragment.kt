package com.g3.spot_guide.screens.spotDetail

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.fragment.BaseFragment
import com.g3.spot_guide.databinding.ImagesPreviewFragmentBinding
import java.io.Serializable

data class ImagesPreviewFragmentArguments(
    val images: List<Uri>,
    val firstImagePosition: Int
) : Serializable

class ImagesPreviewFragment : BaseFragment<ImagesPreviewFragmentBinding, Nothing>() {

    private val arguments: ImagesPreviewFragmentArgs by navArgs()

    private val fragments: List<ImageFragment> by lazy {
        val fragments = mutableListOf<ImageFragment>()
        arguments.args.images.forEach { uri -> fragments.add(ImageFragment(uri)) }
        fragments
    }

    private val adapter: FragmentStatePagerAdapter by lazy {
        object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = fragments[position]
            override fun getCount() = arguments.args.images.size
        }
    }

    override fun setBinding(layoutInflater: LayoutInflater): ImagesPreviewFragmentBinding = ImagesPreviewFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ImagesPreviewFragmentBinding, context: Context) {
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.imagesVP.adapter = adapter
        binding.imagesVP.currentItem = arguments.args.firstImagePosition
    }
}