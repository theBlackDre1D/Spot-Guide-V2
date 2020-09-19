package com.g3.spot_guide.screens.profile.editProfile

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.EditProfileFragmentBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.AppBarView
import com.g3.spot_guide.views.BottomButtonView
import com.g3.spot_guide.views.HeaderWithEditTextOutlinedView
import org.koin.android.viewmodel.ext.android.viewModel

class EditProfileFragment : BaseFragment<EditProfileFragmentBinding, EditProfileFragmentHandler>() {

    private val user: User by lazy { handler.getUser() }

    private val editProfileFragmentViewModel: EditProfileFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): EditProfileFragmentBinding = EditProfileFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: EditProfileFragmentBinding, context: Context) {
        editProfileFragmentViewModel.fillAttributesWithValues(user)
        setupAppBar()
        setupCurrentUserInfo()
        setupBottomButton()
    }

    private fun setupAppBar() {
        val handler = object : AppBarView.AppBarViewHandler {
            override fun onLeftIconClick() { handler.navigateBack() }
        }
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.profile__edit_header, true, R.drawable.ic_arrow_left, handler)
    }

    private fun setupCurrentUserInfo() {
        binding.profilePictureIV.loadImageFromFirebase(user.profilePictureUrl)

        val userNameHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.userName = input
            }
        }
        binding.usernameV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__username, editProfileFragmentViewModel.userName, userNameHandler)

        val fullNameHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.fullName = input
            }
        }
        binding.fullNameV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__full_name, editProfileFragmentViewModel.fullName, fullNameHandler)

        val stanceHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.stance = input
            }
        }
        binding.stanceV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__stance, editProfileFragmentViewModel.stance, stanceHandler)

        val aboutMeHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.aboutMe = input
            }
        }
        binding.aboutMeV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__about_me, editProfileFragmentViewModel.aboutMe, aboutMeHandler)

        val sponsorsHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.sponsors = input
            }
        }
        binding.sponsorsV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__sponsors, editProfileFragmentViewModel.sponsors, sponsorsHandler)

        val instagramHandler = object : HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.instagramUrl = input
            }
        }
        binding.instagramV.configuration = HeaderWithEditTextOutlinedView.HeaderWithEditTextOutlinedViewConfiguration(R.string.profile__instagram, editProfileFragmentViewModel.instagramUrl, instagramHandler)
    }

    private fun setupBottomButton() {
        val handler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                // TODO save
            }
        }
        binding.bottomButtonV.configuration = BottomButtonView.BottomButtonViewConfiguration(R.string.profile__save, handler)
    }
}

interface EditProfileFragmentHandler : BaseFragmentHandler {
    fun getUser(): User
    fun navigateBack()
}