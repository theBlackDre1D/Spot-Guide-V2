package com.g3.spot_guide.screens.login

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.base.Either
import com.g3.spot_guide.databinding.LoginFragmentBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.views.RoundedInputView
import com.g3.spot_guide.views.TwoColorsTextView
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<LoginFragmentBinding, LoginFragmentHandler>() {

    private val loginFragmentViewModel: LoginFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): LoginFragmentBinding = LoginFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: LoginFragmentBinding, context: Context) {
        setupEmailField()
        setupPasswordField()
        setupLoginButton()
        setupNavigationToRegister()
    }

    private fun setupEmailField() {
        val listener = object : RoundedInputView.RoundedInputViewListener {
            override fun onTextChanged(text: String) {
                loginFragmentViewModel.email = text
            }
        }
        val configuration = RoundedInputView.RoundedInputViewConfiguration(R.string.login__email, loginFragmentViewModel.email, false, listener)

        binding.emailRIV.configuration = configuration
    }

    private fun setupPasswordField() {
        val listener = object : RoundedInputView.RoundedInputViewListener {
            override fun onTextChanged(text: String) {
                loginFragmentViewModel.password = text
            }
        }
        val configuration = RoundedInputView.RoundedInputViewConfiguration(R.string.login__password, loginFragmentViewModel.email, true, listener)

        binding.passwordRIV.configuration = configuration
    }

    private fun setupLoginButton() = binding.loginB.onClick { logIn() }

    private fun logIn() {
        loginFragmentViewModel.loggedInUser.observe(this, Observer { result ->
            when (result) {
                is Either.Error -> showSnackBar(binding.root, R.string.error__log_in, true)
                is Either.Success -> {
                    Session.saveAndSetLoggedInUser(result.value)
                    handler.openMapScreen()
                }
            }
        })

        loginFragmentViewModel.logIn()
    }

    private fun setupNavigationToRegister() {
        val configuration = TwoColorsTextView.TwoColorsTextViewConfiguration(R.string.login__no_account, R.string.login__register)
        binding.toRegisterTV.configuration = configuration
    }
}

interface LoginFragmentHandler : BaseFragmentHandler {
    fun openMapScreen()
}