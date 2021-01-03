package com.g3.spot_guide.screens.profile.myProfile

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
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.screens.profile.UserSpotsAdapter
import com.g3.spot_guide.utils.DateUtils
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonView
import com.g3.spot_guide.views.HeaderWithTextView
import com.g3.spot_guide.views.TodaySpotView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.ext.android.viewModel

class MyProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileFragmentHandler>(), UserSpotsAdapter.SpotsAdapterHandler {

    private val userSpotsAdapter: UserSpotsAdapter by lazy { UserSpotsAdapter(this) }

    private val myProfileFragmentViewModel: MyProfileFragmentViewModel by viewModel()
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
                myProfileFragmentViewModel.userEmail = email
                myProfileFragmentViewModel.getUserByEmail(email)
            }
        }
    }

    private fun setupBottomButton() {
        val handler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                val user = myProfileFragmentViewModel.userLiveData.value
                if (user is Either.Success) {
                    user.value.let {
                        handler.openEditProfileScreen(it)
                    }
                }
            }
        }
        binding.bottomButtonV.configuration = BottomButtonView.BottomButtonViewConfiguration(
            R.string.profile__edit,
            handler
        )
    }

    private fun setupAppBar() {
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.bar_menu_profile, false, null, null)
    }

    private fun setupLiveDataObservers() {
        myProfileFragmentViewModel.userLiveData.observe(this, { user ->
            val userValue = user.getValueOrNull()
            if (userValue != null) {
                setupUser(userValue)
                binding.appBarV.showLoading(false)
            } else {
                binding.appBarV.showLoading(false)
                showSnackBar(binding.root, R.string.error__user_loading)
            }
        })

        myProfileFragmentViewModel.userSpots.observe(this, { spots ->
            val value = spots.getValueOrNull()
            if (value != null) {
                handleSpotsLoaded(value)
            } else {
                // TODO spot loading failure
            }
        })
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
        binding.nameTV.text = user.fullName
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

        myProfileFragmentViewModel.getUsersSpots(user.id)

        setupTodaySpot(user)
    }

    private fun setupTodaySpot(user: User) {
        binding.todaySpotV.isVisible = user.todaySpot != null && DateUtils.isTodaySpotValid(user.todaySpot)
        val todaySpotHandler = object : TodaySpotView.TodaySpotViewHandler {
            override fun onBubbleClick() {
                user.todaySpot?.let { todaySpot ->
                    handler.fromMyProfileToTodaySpot(todaySpot)
                }
            }
        }
        binding.todaySpotV.configuration = TodaySpotView.TodaySpotViewConfiguration(user.todaySpot?.spotName ?: "", todaySpotHandler)
    }

    override fun onSpotClick(spot: Spot) = handler.openSpotDetail(spot)
}

interface ProfileFragmentHandler : BaseFragmentHandler {
    fun openEditProfileScreen(user: User)
    fun openProfileFragment(user: User)
    fun openSpotDetail(spot: Spot)
    fun openInstagramAccount(instagramNick: String)
    fun fromMyProfileToTodaySpot(todaySpot: TodaySpot)
}