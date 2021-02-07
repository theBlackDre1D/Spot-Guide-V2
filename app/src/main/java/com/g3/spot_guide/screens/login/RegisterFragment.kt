package com.g3.spot_guide.screens.login

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.RegisterFragmentBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.RoundedInputView
import com.g3.spot_guide.views.TwoColorsTextView
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<RegisterFragmentBinding, RegisterFragmentHandler>() {

    private val registerFragmentViewModel: RegisterFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): RegisterFragmentBinding = RegisterFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: RegisterFragmentBinding, context: Context) {
        setupRegisterListener()
        setupRegisterButton()
        setupBottomText()
        checkAllFields()
    }

    override fun onFragmentResumed() {
        setupInputViews()
    }

    private fun setupInputViews() {
        val userNameListener = object : RoundedInputView.RoundedInputViewListener {
            override fun onTextChanged(text: String) {
                registerFragmentViewModel.userName = text
                checkAllFields()
            }
        }
        binding.userNameRIV.configuration = RoundedInputView.RoundedInputViewConfiguration(R.string.login__username, registerFragmentViewModel.userName, false, userNameListener)

        val emailListener = object : RoundedInputView.RoundedInputViewListener {
            override fun onTextChanged(text: String) {
                registerFragmentViewModel.email = text
                checkAllFields()
            }
        }
        binding.emailRIV.configuration = RoundedInputView.RoundedInputViewConfiguration(R.string.login__email, registerFragmentViewModel.email, false, emailListener)

        val passwordListener = object : RoundedInputView.RoundedInputViewListener {
            override fun onTextChanged(text: String) {
                registerFragmentViewModel.password = text
                checkAllFields()
            }
        }
        binding.passwordRIV.configuration = RoundedInputView.RoundedInputViewConfiguration(R.string.login__password, registerFragmentViewModel.password, true, passwordListener)
    }

    private fun setupRegisterListener() {
        registerFragmentViewModel.registerResult.observe(this, { resultEither ->
            val result = resultEither.getValueOrNull()
            if (result != null) {
                registerFragmentViewModel.getCurrentUser()
            } else {
                setLoadingMode(false)
                showSnackBar(binding.root, R.string.error__register, true)
            }
        })

        registerFragmentViewModel.userAfterRegister.observe(this, { userEither ->
            userEither.getValueOrNull()?.let { user ->
                Session.saveAndSetLoggedInUser(user)
                handler.fromRegisterToEditProfile(user)
            }
        })
    }

    private fun setupRegisterButton() {
        binding.registerB.onClick {
            setLoadingMode(true)
            registerFragmentViewModel.registerUser()
        }
    }

    private fun setLoadingMode(isLoading: Boolean) {
        handler.setLoginProgress(isLoading)
        binding.loadingV.isVisible = isLoading
        binding.registerB.isVisible = !isLoading
    }

    private fun checkAllFields() {
        binding.registerB.isEnabled = registerFragmentViewModel.email.isNotBlank() && registerFragmentViewModel.password.isNotBlank() && registerFragmentViewModel.userName.isNotBlank()
    }

    private fun setupBottomText() {
        binding.toLoginV.configuration = TwoColorsTextView.TwoColorsTextViewConfiguration(R.string.login__existing_account, R.string.login__login)
        binding.toLoginV.onClick { handler.navigateBack() }
    }

    override fun onFragmentDestroyed() {
        handler.setLoginProgress(false)
        super.onFragmentDestroyed()
    }
}

interface RegisterFragmentHandler : BaseFragmentHandler {
    fun navigateBack()
    fun fromRegisterToEditProfile(user: User)
    fun setLoginProgress(isLoggingIn: Boolean)
}