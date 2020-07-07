package com.g3.spot_guide.screens.gallery

import android.Manifest
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.araujo.jordan.excuseme.ExcuseMe
import com.g3.spot_guide.R
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.databinding.GalleryFragmentBinding
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonsView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryFragment : BaseFragment<GalleryFragmentBinding, GalleryFragmentViewModel, GalleryFragmentHandler>(),
    GalleryAdapter.GalleryAdapterHandler, BottomButtonsView.BottomButtonsViewListener, AppBarView.AppBarViewHandler {

    private val adapter: GalleryAdapter by lazy { GalleryAdapter(this) }

    override val viewModel: GalleryFragmentViewModel by viewModels { GalleryFragmentViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): GalleryFragmentBinding = GalleryFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: GalleryFragmentBinding, context: Context) {
        setupPhotosRV()
        setupImagesObserver()
        handlePermissions()
        setupBottomButtons()
        setupAppBar()
    }

    private fun setupImagesObserver() {
        viewModel.images.observe(this, Observer { images ->
            when (images) {
                is Either.Error -> showSnackBar(binding.root, R.string.error__images_load)
                is Either.Success -> showImagesInRV(images.value)
            }
        })
    }

    private fun setupPhotosRV() {
        binding.photosRV.adapter = adapter
        binding.photosRV.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun handlePermissions() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = ExcuseMe.couldYouGive(requireContext()).permissionFor(Manifest.permission.READ_EXTERNAL_STORAGE)
            if (result) {
                viewModel.getImagesFromStorage()
            } else {
                showSnackBar(binding.root, R.string.error__images_load)
            }
        }
    }

    private fun showImagesInRV(images: List<ImageModel>) {
        val items = mutableListOf<GalleryAdapter.GalleryAdapterItem>()
        handler.getPickedImages().value?.let { pickedImages ->
            images.forEach { imageModel ->
                items.add(GalleryAdapter.GalleryAdapterItem(imageModel, pickedImages.contains(imageModel)))
            }
        }

        adapter.injectData(items)
    }

    private fun setupBottomButtons() {
        val configuration = BottomButtonsView.BottomButtonsViewConfiguration(R.string.gallery__camera, R.string.gallery__confirm, this)
        binding.bottomButtonsV.configuration = configuration
    }

    private fun setupAppBar() {
        val configuration = AppBarView.AppBarViewConfiguration(R.string.gallery__screen_title, true, this)
        binding.appBarV.configuration = configuration
    }

    override fun onImageClick(imageModel: ImageModel) {
        handler.onImageClick(imageModel)
    }

    override fun onLeftButtonClick() {
        handler.navigateBack()
    }

    override fun onRightButtonClick() {
        handler.navigateBack()
    }

    override fun onBackArrowClick() {
        handler.navigateBack()
    }

}

interface GalleryFragmentHandler : BaseFragmentHandler {
    fun navigateBack()
    fun getPickedImages(): MutableLiveData<List<ImageModel>>
    fun onImageClick(imageModel: ImageModel)
}