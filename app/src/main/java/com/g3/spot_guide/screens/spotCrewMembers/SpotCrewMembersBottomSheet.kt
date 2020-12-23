package com.g3.spot_guide.screens.spotCrewMembers

import android.content.Context
import android.view.LayoutInflater
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.SpotCrewMembersBottomSheetBinding
import com.g3.spot_guide.models.User
import java.io.Serializable

data class SpotCrewMembersBottomSheetParams(
    val crewMembers: List<User>
) : Serializable

class SpotCrewMembersBottomSheet : BaseBottomSheet<SpotCrewMembersBottomSheetBinding, SpotCrewMembersBottomSheetHandler>() {

    private val arguments: SpotCrewMembersBottomSheetArgs by navArgs()

    private val adapter: SpotCrewMembersAdapter by lazy { SpotCrewMembersAdapter() }

    override fun setBinding(layoutInflater: LayoutInflater) = SpotCrewMembersBottomSheetBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotCrewMembersBottomSheetBinding, context: Context) {
        binding.spotCrewMembersRV.adapter = adapter

        val adapterItems = mutableListOf<SpotCrewMembersAdapter.SpotCrewMembersAdapterItem>()
        arguments.args.crewMembers.forEach { crewMember ->
            adapterItems.add(SpotCrewMembersAdapter.SpotCrewMembersAdapterItem(crewMember))
        }

        adapter.injectData(adapterItems)
    }
}

interface SpotCrewMembersBottomSheetHandler : BaseFragmentHandler