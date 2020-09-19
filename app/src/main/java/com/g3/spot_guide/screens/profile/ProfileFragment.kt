package com.g3.spot_guide.screens.profile

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.g3.base.either.Either
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.ProfileFragmentBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonView
import com.g3.spot_guide.views.HeaderWithTextView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileFragmentHandler>(), SpotsAdapter.SpotsAdapterHandler {

    private val spotsAdapter: SpotsAdapter by lazy { SpotsAdapter(this) }

    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): ProfileFragmentBinding = ProfileFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ProfileFragmentBinding, context: Context) {
        setupLiveDataObservers()

        getUser()
        setupBottomButton()
        setupAppBar()
    }

    private fun getUser() {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        currentFirebaseUser?.let { user ->
            user.email?.let { email ->
                profileFragmentViewModel.userEmail = email
                profileFragmentViewModel.getUserByEmail(email)
            }
        }
    }

    private fun setupBottomButton() {
        val handler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                val user = profileFragmentViewModel.userLiveData.value
                if (user is Either.Success) {
                    user.value?.let {
                        handler.openEditProfileScreen(it)
                    }
                }
            }
        }
        binding.bottomButtonV.configuration = BottomButtonView.BottomButtonViewConfiguration(R.string.profile__edit, handler)
    }

    private fun setupAppBar() {
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.bar_menu_profile, false, null, null)
    }

    private fun setupLiveDataObservers() {
        profileFragmentViewModel.userLiveData.observe(this, Observer { user ->
            when (user) {
                is Either.Error -> showSnackBar(binding.root, R.string.error__user_loading)
                is Either.Success -> handleUserLoadRespond(user.value)
            }
        })

        profileFragmentViewModel.userSpots.observe(this, Observer { spots ->
            when (spots) {
                is Either.Error -> {}
                is Either.Success -> handleSpotsLoaded(spots.value)
            }
        })


    }

    private fun handleSpotsLoaded(spots: List<Spot>) {
        val items = mutableListOf<SpotsAdapter.SpotsAdapterItem>()
        spots.forEach { spot ->
            items.add(SpotsAdapter.SpotsAdapterItem(spot))
        }
        spotsAdapter.injectData(items)
    }

    private fun handleUserLoadRespond(user: User?) {
        if (user != null) {
            setupUser(user)
        } else {
            profileFragmentViewModel.userEmail?.let { email ->
                handler.openEditProfileScreen(User(email = email))
            }
        }
    }

    private fun setupUser(user: User) {
        // TODO friends loading

        binding.profilePictureIV.loadImageFromFirebase(user.profilePictureUrl)

        binding.nickTV.text = user.nick
        binding.nameTV.text = "${user.firstName} ${user.lastName}"
        binding.stanceTV.text = user.stance
        binding.aboutMeV.configuration = HeaderWithTextView.HeaderWithTextViewConfiguration(R.string.profile__about_me, user.aboutMe)
        binding.sponsorsV.configuration = HeaderWithTextView.HeaderWithTextViewConfiguration(R.string.profile__sponsors, user.sponsors)

        val instagramHandler = object : HeaderWithTextView.HeaderWithTextViewHandler {
            override fun onTextClicked() {
                // TODO open isntagram
            }

        }
        binding.instagramV.configuration = HeaderWithTextView.HeaderWithTextViewConfiguration(R.string.profile__instagram, user.instagramUrl, R.color.blue_6C63FF, instagramHandler)
        binding.spotsRV.adapter = spotsAdapter

        profileFragmentViewModel.getUsersSpots(user.id)
    }

    override fun onSpotClick(spot: Spot) = handler.openSpotDetail(spot)
}

interface ProfileFragmentHandler : BaseFragmentHandler {
    fun openEditProfileScreen(user: User)
    fun openProfileFragment(user: User)
    fun openSpotDetail(spot: Spot)
}