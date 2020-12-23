package com.g3.spot_guide

import com.g3.spot_guide.providers.SpotFirestoreProvider
import com.g3.spot_guide.providers.UserFirestoreProvider
import com.g3.spot_guide.repositories.ImagesRepository
import com.g3.spot_guide.repositories.SpotRepository
import com.g3.spot_guide.repositories.UserRepository
import com.g3.spot_guide.screens.addSpot.AddSpotActivityViewModel
import com.g3.spot_guide.screens.addSpot.AddSpotFragmentViewModel
import com.g3.spot_guide.screens.crew.CrewFragmentViewModel
import com.g3.spot_guide.screens.gallery.GalleryFragmentViewModel
import com.g3.spot_guide.screens.login.LoginFragmentViewModel
import com.g3.spot_guide.screens.login.RegisterFragmentViewModel
import com.g3.spot_guide.screens.map.MapActivityViewModel
import com.g3.spot_guide.screens.map.MapFragmentViewModel
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivityViewModel
import com.g3.spot_guide.screens.profile.editProfile.EditProfileFragmentViewModel
import com.g3.spot_guide.screens.profile.myProfile.MyProfileFragmentViewModel
import com.g3.spot_guide.screens.profile.otherUserProfile.OtherUserProfileActivityViewModel
import com.g3.spot_guide.screens.profile.otherUserProfile.OtherUserProfileFragmentViewModel
import com.g3.spot_guide.screens.splash.SplashActivityViewModel
import com.g3.spot_guide.screens.spotDetail.SpotDetailFragmentViewModel
import com.g3.spot_guide.screens.todaySpot.addTodaySpot.AddTodaySpotBottomSheetFragmentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    // ViewModels
    viewModel { AddSpotActivityViewModel() }
    viewModel { AddSpotFragmentViewModel( get() ) }
    viewModel { SpotDetailFragmentViewModel( get(), get() ) }
    viewModel { MapFragmentViewModel( get() ) }
    viewModel { GalleryFragmentViewModel( get() ) }
    viewModel { MapActivityViewModel( get() ) }
    viewModel { LoginFragmentViewModel( get() ) }
    viewModel { RegisterFragmentViewModel( get() ) }
    viewModel { MyProfileFragmentViewModel( get(), get() ) }
    viewModel { EditProfileActivityViewModel() }
    viewModel { EditProfileFragmentViewModel( get() ) }
    viewModel { CrewFragmentViewModel( get() ) }
    viewModel { SplashActivityViewModel( get() ) }
    viewModel { OtherUserProfileActivityViewModel() }
    viewModel { OtherUserProfileFragmentViewModel( get(), get() ) }
    viewModel { AddTodaySpotBottomSheetFragmentViewModel( get() ) }

    // Repositories
    single { SpotRepository( get() ) }
    single { ImagesRepository() }
    single { UserRepository( get() ) }

    // Providers
    factory { SpotFirestoreProvider() }
    factory { UserFirestoreProvider() }
}