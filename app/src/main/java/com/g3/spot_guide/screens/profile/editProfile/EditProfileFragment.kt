package com.g3.spot_guide.screens.profile.editProfile

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LiveData
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.EditProfileFragmentBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.loadImageFromImageModel
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.ImageModel
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
        if (editProfileFragmentViewModel.isFirstLaunch) {
            editProfileFragmentViewModel.fillAttributesWithValues(user)
            editProfileFragmentViewModel.isFirstLaunch = false
        }
        setupAppBar()
        setupCurrentUserInfo()
        setupBottomButton()
        setupUserSavedObserver()
        setupChangeProfilePicture()
    }

    private fun setupAppBar() {
        val handler = object : AppBarView.AppBarViewHandler {
            override fun onLeftIconClick() { handler.navigateBack() }
        }
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.profile__edit_header, true, null, handler)
    }

    private fun setupCurrentUserInfo() {
        if (handler.getProfilePictureLiveData().value == null) {
            binding.profilePictureIV.loadImageFromFirebase(user.profilePictureUrl)
        }

        val userNameHandler = object : HeaderWithEditTextView.HeaderWithEditTextViewHandler {
            override fun onInputTextChanged(input: String) {
                editProfileFragmentViewModel.nick = input
            }
        }
        binding.usernameV.configuration = HeaderWithEditTextView.HeaderWithEditTextViewConfiguration(R.string.profile__username, editProfileFragmentViewModel.nick, userNameHandler)

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
                binding.appBarV.showLoading(canSave)
                if (canSave) {
                    context?.let { nonNullContext ->
                        editProfileFragmentViewModel.saveUser(nonNullContext)
                    }
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

    private fun setupUserSavedObserver() {
        editProfileFragmentViewModel.userSaved.observe(this, { userEither ->
            binding.appBarV.showLoading(false)
            val user = userEither.getValueOrNull()
            if (user != null) {
                Session.saveAndSetLoggedInUser(user)
                showSnackBar(binding.root, R.string.profile__user_saved)
                handler.onEditInfoSuccess()
            } else {
                showSnackBar(binding.root, R.string.error__user_not_saved)
            }
        })
    }

    private fun setupChangeProfilePicture() {
        binding.changeProfilePictureB.onClick {
            handler.fromEditProfileToGallery()
        }

        val profilePictureLiveData = handler.getProfilePictureLiveData()
        profilePictureLiveData.observe(this, { imageModel ->
            editProfileFragmentViewModel.profilePicture = imageModel
            imageModel?.let {
                binding.profilePictureIV.loadImageFromImageModel(imageModel)
            }
        })
    }
}

interface EditProfileFragmentHandler : BaseFragmentHandler {
    fun getUser(): User
    fun navigateBack()
    fun fromEditProfileToGallery()
    fun getProfilePictureLiveData(): LiveData<ImageModel?>
    fun onEditInfoSuccess()
}