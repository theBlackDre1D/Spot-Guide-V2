package com.g3.spot_guide

import com.g3.spot_guide.providers.SpotFirestoreProvider
import com.g3.spot_guide.providers.UserFirestoreProvider
import com.g3.spot_guide.repositories.ImagesRepository
import com.g3.spot_guide.repositories.SpotRepository
import com.g3.spot_guide.repositories.UserRepository
import com.g3.spot_guide.screens.addSpot.AddSpotActivityViewModel
import com.g3.spot_guide.screens.addSpot.AddSpotFragmentViewModel
import com.g3.spot_guide.screens.gallery.GalleryFragmentViewModel
import com.g3.spot_guide.screens.login.LoginFragmentViewModel
import com.g3.spot_guide.screens.map.MapActivityViewModel
import com.g3.spot_guide.screens.map.MapFragmentViewModel
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    // ViewModels
    viewModel { AddSpotActivityViewModel() }
    viewModel { AddSpotFragmentViewModel( get() ) }
    viewModel { SpotDetailFragmentViewModel( get() ) }
    viewModel { MapFragmentViewModel( get() ) }
    viewModel { GalleryFragmentViewModel( get() ) }
    viewModel { MapActivityViewModel() }
    viewModel { LoginFragmentViewModel( get() ) }

    // Repositories
    single { SpotRepository( get() ) }
    single { ImagesRepository() }
    single { UserRepository( get() ) }

    // Providers
    factory { SpotFirestoreProvider() }
    factory { UserFirestoreProvider() }
}