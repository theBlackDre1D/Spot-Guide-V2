package com.g3.spot_guide.screens.gallery

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.models.ImageModel
import com.g3.spot_guide.repositories.ImagesRepository
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class GalleryFragmentViewModel(private val savedState: SavedStateHandle, session: Session) : AndroidViewModel(session) {

    private val imagesRepository: ImagesRepository by lazy { ImagesRepository() }

    val images = MutableLiveData<Either<List<ImageModel>>>()

    fun getImagesFromStorage() {
        viewModelScope.launch {
            images.postValue(imagesRepository.loadImages())
        }
    }

    class ViewModelInstanceFactory(owner: SavedStateRegistryOwner, bundle: Bundle? = null) : AbstractSavedStateViewModelFactory(owner, bundle) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T = GalleryFragmentViewModel(handle, Session.application) as T
    }
}