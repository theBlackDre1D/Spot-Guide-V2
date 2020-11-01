package com.g3.spot_guide.screens.crew

import android.content.Context
import android.view.LayoutInflater
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.CrewFragmentBinding
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.AppBarView
import com.google.android.material.tabs.TabLayout
import org.koin.android.viewmodel.ext.android.viewModel

class CrewFragment : BaseFragment<CrewFragmentBinding, CrewFragmentHandler>(), CrewMembersAdapter.CrewMembersAdapterHandler,
    CrewMemberRequestsAdapter.CrewMemberRequestsAdapterHandler {

    private val profileFragmentViewModel: CrewFragmentViewModel by viewModel()
    private val membersAdapter: CrewMembersAdapter by lazy { CrewMembersAdapter(this) }
    private val memberRequestsAdapter: CrewMemberRequestsAdapter by lazy { CrewMemberRequestsAdapter(this) }

    override fun setBinding(layoutInflater: LayoutInflater): CrewFragmentBinding = CrewFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: CrewFragmentBinding, context: Context) {
        setupAppbar()
        setupTabsLayout()
        setupObservers()
        getInitialRequests()

        binding.crewRV.adapter = membersAdapter
    }

    private fun setupAppbar() {
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.crew__screen_header, false, null, null)
    }

    private fun setupTabsLayout() {
        binding.tabsTL.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (tab.text == getString(R.string.crew__members)) {
                        binding.crewRV.adapter = membersAdapter
                        binding.appBarV.showLoading(true)
                        profileFragmentViewModel.getCrewMembers()
                    } else {
                        binding.crewRV.adapter = memberRequestsAdapter
                        binding.appBarV.showLoading(true)
                        profileFragmentViewModel.getCrewMemberRequests()
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        profileFragmentViewModel.crewMembers.observe(this, { crewMembers ->
            val adapterItems = mutableListOf<CrewMembersAdapter.CrewMembersAdapterItem>()
            crewMembers.forEach { memberEither ->
                memberEither.getValueOrNull()?.let { user ->
                    adapterItems.add(CrewMembersAdapter.CrewMembersAdapterItem(user))
                }
            }

            membersAdapter.injectData(adapterItems)
        })

        profileFragmentViewModel.crewMemberRequests.observe(this, { memberRequests ->
            val adapterItems = mutableListOf<CrewMemberRequestsAdapter.CrewMemberRequestsAdapterItem>()
            memberRequests.forEach { requestEither ->
                requestEither.getValueOrNull()?.let { user ->
                    adapterItems.add(CrewMemberRequestsAdapter.CrewMemberRequestsAdapterItem(user))
                }
            }

            memberRequestsAdapter.injectData(adapterItems)
        })
    }

    private fun getInitialRequests() {
        binding.appBarV.showLoading(true)
        profileFragmentViewModel.getCrewMembers()
        profileFragmentViewModel.getCrewMemberRequests()
    }

    override fun onSpotClick(spotId: String) {
        handler.onSpotClick(spotId)
    }

    override fun onCrewMemberClick(member: User) {
        handler.onCrewMemberClick(member)
    }

    override fun onRequestDecision(accept: Boolean, userId: String) {
        // TODO finish member request decision
    }

    override fun onInstagramClick(instagramNick: String) {
        handler.openInstagramAccount(instagramNick)
    }

    override fun itemsLoaded() {
        binding.appBarV.showLoading(false)
    }
}

interface CrewFragmentHandler : BaseFragmentHandler {
    fun onCrewMemberClick(member: User)
    fun onSpotClick(spotId: String)
    fun openInstagramAccount(instagramNick: String)
}