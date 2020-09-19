package com.g3.spot_guide.screens.crew

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.CrewFragmentBinding
import com.g3.spot_guide.models.User

class CrewFragment : BaseFragment<CrewFragmentBinding, CrewFragmentHandler>() {

    override fun setBinding(layoutInflater: LayoutInflater): CrewFragmentBinding = CrewFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: CrewFragmentBinding, context: Context) {

    }
}

interface CrewFragmentHandler : BaseFragmentHandler {
    fun onCrewMemberClicked(user: User)
}