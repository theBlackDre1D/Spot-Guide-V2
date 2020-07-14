package com.g3.spot_guide.screens.spotDetail

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.databinding.ImagePreviewFragmentBinding
import com.g3.spot_guide.extensions.loadImageFromUri

class ImageFragment(
    private val imageUri: Uri
) : BaseFragment<ImagePreviewFragmentBinding, Nothing>(){

    override fun setBinding(layoutInflater: LayoutInflater): ImagePreviewFragmentBinding = ImagePreviewFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ImagePreviewFragmentBinding, context: Context) {
        binding.imageZV.loadImageFromUri(imageUri)
    }
}