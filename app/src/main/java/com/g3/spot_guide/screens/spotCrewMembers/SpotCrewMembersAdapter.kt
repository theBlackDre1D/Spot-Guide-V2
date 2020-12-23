package com.g3.spot_guide.screens.spotCrewMembers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.SpotCrewMemberItemBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.models.User

class SpotCrewMembersAdapter : BaseRecyclerViewAdapter<SpotCrewMembersAdapter.SpotCrewMembersAdapterViewHolder, SpotCrewMembersAdapter.SpotCrewMembersAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): SpotCrewMembersAdapterViewHolder {
        return SpotCrewMembersAdapterViewHolder(SpotCrewMemberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: SpotCrewMembersAdapterViewHolder, adapterItem: SpotCrewMembersAdapterItem, position: Int) {
        holder.picture.loadImageFromFirebase(adapterItem.user.profilePictureUrl)
        holder.userName.text = adapterItem.user.fullName
        holder.time.text = adapterItem.user.todaySpot?.time
    }

    class SpotCrewMembersAdapterViewHolder(private val binding: SpotCrewMemberItemBinding) : BaseViewHolder(binding.root) {
        val picture = binding.userProfilePictureIV
        val userName = binding.userNameTV
        val time = binding.timeTV
    }

    class SpotCrewMembersAdapterItem(val user: User) : BaseAdapterItem()
}