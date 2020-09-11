package com.g3.spot_guide.screens.profile

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileFragmentHandler>() {

    override fun setBinding(layoutInflater: LayoutInflater): ProfileFragmentBinding = ProfileFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: ProfileFragmentBinding, context: Context) {
        getUser()
    }

    private fun getUser() {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        currentFirebaseUser?.let { user ->
            user
        }
    }
}

interface ProfileFragmentHandler : BaseFragmentHandler {

}