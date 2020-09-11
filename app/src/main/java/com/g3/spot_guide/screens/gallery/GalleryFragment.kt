package com.g3.spot_guide.screens.gallery

import android.Manifest
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.araujo.jordan.excuseme.ExcuseMe
import com.g3.base.either.Either
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.GalleryFragmentBinding
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonsView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class GalleryFragment : BaseFragment<GalleryFragmentBinding, GalleryFragmentHandler>(),
    GalleryAdapter.GalleryAdapterHandler, BottomButtonsView.BottomButtonsViewListener, AppBarView.AppBarViewHandler {

    private val adapter: GalleryAdapter by lazy { GalleryAdapter(this) }

    private val galleryFragmentViewModel: GalleryFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): GalleryFragmentBinding = GalleryFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: GalleryFragmentBinding, context: Context) {
        setupPhotosRV()
        setupImagesObserver()
        handlePermissions()
        setupBottomButtons()
        setupAppBar()
    }

    private fun setupImagesObserver() {
        galleryFragmentViewModel.images.observe(this, Observer { images ->
            when (images) {
                is Either.Error -> showSnackBar(binding.root, R.string.error__images_load)
                is Either.Success<List<ImageModel>> -> showImagesInRV(images.value)
            }
        })
    }

    private fun setupPhotosRV() {
        binding.photosRV.adapter = adapter
        binding.photosRV.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    private fun handlePermissions() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = ExcuseMe.couldYouGive(requireContext()).permissionFor(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (result.granted.size == 2) {
                galleryFragmentViewModel.getImagesFromStorage()
            } else {
                showSnackBar(binding.root, R.string.error__images_load)
            }
        }
    }

    private fun showImagesInRV(images: List<ImageModel>) {
        val items = mutableListOf<GalleryAdapter.GalleryAdapterItem>()
        images.forEach { imageModel ->
            items.add(GalleryAdapter.GalleryAdapterItem(imageModel, galleryFragmentViewModel.pickedImages.contains(imageModel)))
        }

        adapter.injectData(items)
    }

    private fun setupBottomButtons() {
        val configuration = BottomButtonsView.BottomButtonsViewConfiguration(R.string.gallery__camera, R.string.gallery__confirm, this)
        binding.bottomButtonsV.configuration = configuration
    }

    private fun setupAppBar() {
        val configuration = AppBarView.AppBarViewConfiguration(R.string.gallery__screen_title, true, false, null, this)
        binding.appBarV.configuration = configuration
    }

    override fun onImageClick(imageModel: ImageModel) {
        if (galleryFragmentViewModel.pickedImages.contains(imageModel)) {
            galleryFragmentViewModel.pickedImages.remove(imageModel)
        } else {
            galleryFragmentViewModel.pickedImages.add(imageModel)
        }
    }

    override fun onLeftButtonClick() {
        // TODO
    }

    override fun onRightButtonClick() {
        handler.savePickedImages(galleryFragmentViewModel.pickedImages)
        handler.navigateBack()
    }

    override fun onLeftIconClick() {
        handler.navigateBack()
    }

}

interface GalleryFragmentHandler : BaseFragmentHandler {
    fun navigateBack()
    fun savePickedImages(images: List<ImageModel>)
}