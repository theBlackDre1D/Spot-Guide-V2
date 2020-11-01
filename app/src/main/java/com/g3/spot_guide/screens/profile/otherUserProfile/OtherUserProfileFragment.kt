package com.g3.spot_guide.screens.profile.otherUserProfile

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.g3.base.either.Either
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.ProfileFragmentBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.profile.UserSpotsAdapter
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.HeaderWithTextView
import org.koin.android.viewmodel.ext.android.viewModel

class OtherUserProfileFragment : BaseFragment<ProfileFragmentBinding, OtherUserProfileFragmentHandler>(), UserSpotsAdapter.SpotsAdapterHandler {

    private val userSpotsAdapter: UserSpotsAdapter by lazy { UserSpotsAdapter(this) }

    private val otherUserProfileFragmentViewModel: OtherUserProfileFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater) = ProfileFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ProfileFragmentBinding, context: Context) {
        otherUserProfileFragmentViewModel.user.postValue(Either.Success(handler.getUser()))

        setupObservers()
        setupBottomButton()
        setupAppBar()
    }

    private fun setupObservers() {
        otherUserProfileFragmentViewModel.user.observe(this, {
            val user = it.getValueOrNull()
            user?.let { setupUser(user) }
        })

        otherUserProfileFragmentViewModel.userSpots.observe(this, { spots ->
            val value = spots.getValueOrNull()
            if (value != null) {
                handleSpotsLoaded(value)
            } else {
               // TODO spot loading failure
            }
        })
    }

    private fun setupBottomButton() {
        binding.bottomButtonV.isVisible = false
    }

    private fun setupAppBar() {
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.bar_menu_profile, false, null, null)
    }

    private fun handleSpotsLoaded(spots: List<Spot>) {
        val items = mutableListOf<UserSpotsAdapter.UserSpotsAdapterItem>()
        spots.forEach { spot ->
            items.add(UserSpotsAdapter.UserSpotsAdapterItem(spot))
        }
        userSpotsAdapter.injectData(items)
    }

    private fun setupUser(user: User) {
        binding.profilePictureIV.loadImageFromFirebase(user.profilePictureUrl)

        binding.nickTV.text = user.nick
        binding.nameTV.text = "${user.firstName} ${user.lastName}"
        binding.stanceTV.text = user.stance
        binding.aboutMeV.configuration = HeaderWithTextView.HeaderWithTextViewConfiguration(
            R.string.profile__about_me,
            user.aboutMe
        )
        binding.sponsorsV.configuration = HeaderWithTextView.HeaderWithTextViewConfiguration(
            R.string.profile__sponsors,
            user.sponsors
        )

        binding.instagramNickTV.text = user.instagramNick
        binding.instagramNickTV.onClick {
            user.instagramNick?.let { nick ->
                handler.openInstagramAccount(nick)
            }
        }

        binding.spotsRV.adapter = userSpotsAdapter

        otherUserProfileFragmentViewModel.getUsersSpots(user.id)
    }

    override fun onSpotClick(spot: Spot) {
        handler.openSpotDetailScreen(spot)
    }
}

interface OtherUserProfileFragmentHandler : BaseFragmentHandler {
    fun getUser(): User
    fun openInstagramAccount(instagramNick: String)
    fun openSpotDetailScreen(spot: Spot)
}