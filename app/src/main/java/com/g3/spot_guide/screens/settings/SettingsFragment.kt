package com.g3.spot_guide.screens.settings

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.SettingsFragmentBinding
import com.g3.spot_guide.extensions.onClick
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : BaseFragment<SettingsFragmentBinding, SettingsFragmentHandler>() {

    override fun setBinding(layoutInflater: LayoutInflater): SettingsFragmentBinding = SettingsFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SettingsFragmentBinding, context: Context) {
        binding.logOutB.onClick {
            FirebaseAuth.getInstance().signOut()
            handler.fromSettingsToLogin()
        }
    }
}

interface SettingsFragmentHandler : BaseFragmentHandler {
    fun fromSettingsToLogin()
}