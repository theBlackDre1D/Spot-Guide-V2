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
import com.g3.spot_guide.views.HeaderWithEditTextView
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
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

        val userNameHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.userName = input
            }
        }
        binding.usernameV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__username, editProfileFragmentViewModel.userName, userNameHandler)

        val fullNameHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.fullName = input
            }
        }
        binding.fullNameV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__full_name, editProfileFragmentViewModel.fullName, fullNameHandler)

        val stanceHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.stance = input
            }
        }
        binding.stanceV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__stance, editProfileFragmentViewModel.stance, stanceHandler)

        val aboutMeHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.aboutMe = input
            }
        }
        binding.aboutMeV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__about_me, editProfileFragmentViewModel.aboutMe, aboutMeHandler)

        val sponsorsHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.sponsors = input
            }
        }
        binding.sponsorsV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__sponsors, editProfileFragmentViewModel.sponsors, sponsorsHandler)

        val instagramHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.instagramNick = input
            }
        }
        binding.instagramV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__instagram, editProfileFragmentViewModel.instagramNick ?: "", instagramHandler)
    }

    private fun setupBottomButton() {
        val handler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                val canSave = validateAllFields()
                if (canSave) {
                    // TODO save
                }
            }
        }
        binding.bottomButtonV.configuration = BottomButtonView.BottomButtonViewConfiguration(R.string.profile__save, handler)
    }

    private fun validateAllFields(): Boolean {
        var fieldsAreValid = true
        val allFields = listOf(binding.usernameV, binding.fullNameV, binding.stanceV, binding.aboutMeV, binding.sponsorsV, binding.instagramV)
        allFields.forEach { field ->
            val isValid = field.binding.inputET.validator()
                .nonEmpty()
                .addErrorCallback { errorText ->
                    field.binding.inputET.error = errorText
                }
                .check()
            if (!isValid) {
                fieldsAreValid = false
            }
        }

        return fieldsAreValid
    }
}

interface EditProfileFragmentHandler : BaseFragmentHandler {
    fun getUser(): User
    fun navigateBack()
}