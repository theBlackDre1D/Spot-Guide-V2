package com.g3.spot_guide.screens.profile.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import com.g3.base.screens.activity.BaseActivity
import com.g3.base.screens.activity.BaseParameters
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.EditProfileActivityBinding
import com.g3.spot_guide.models.User
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

const val EDIT_PROFILE_PARAMETERS__EXTRAS_KEY = "EDIT_PROFILE_PARAMETERS__EXTRAS_KEY"

class EditProfileActivity : BaseActivity<EditProfileActivityBinding, EditProfileActivity.Arguments>(), EditProfileFragmentHandler {

    inner class Arguments : BaseParameters {
        override fun loadParameters(extras: Bundle) {
            editProfileActivityViewModel.activityParams = extras.getSerializable(EDIT_PROFILE_PARAMETERS__EXTRAS_KEY) as Parameters
        }
    }

    data class Parameters(
        val user: User
    ) : Serializable

    private val editProfileActivityViewModel: EditProfileActivityViewModel by viewModel()
    override fun setNavigationGraph() = R.id.editProfileNavigationContainer
    override fun createParameters() = Arguments()
    override fun setBinding(layoutInflater: LayoutInflater): EditProfileActivityBinding = EditProfileActivityBinding.inflate(layoutInflater)
    override fun onActivityLoadingFinished(binding: EditProfileActivityBinding) {
        editProfileActivityViewModel.activityParams
    }

    override fun getUser() = editProfileActivityViewModel.activityParams.user
    override fun navigateBack() = super.onBackPressed()
}