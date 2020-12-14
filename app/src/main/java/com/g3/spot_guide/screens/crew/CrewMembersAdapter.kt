package com.g3.spot_guide.screens.crew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.CrewMemberItemViewBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.utils.SpotUtils
import com.g3.spot_guide.views.TodaySpotView

class CrewMembersAdapter(
    private val handler: CrewMembersAdapterHandler
) : BaseRecyclerViewAdapter<CrewMembersAdapter.CrewMembersAdapterViewHolder, CrewMembersAdapter.CrewMembersAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): CrewMembersAdapterViewHolder {
        return CrewMembersAdapterViewHolder(CrewMemberItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: CrewMembersAdapterViewHolder, adapterItem: CrewMembersAdapterItem, position: Int) {
        holder.profilePicture.loadImageFromFirebase(adapterItem.user.profilePictureUrl)
        holder.nick.text = adapterItem.user.nick
        holder.realName.text = adapterItem.user.fullName
        holder.stance.text = adapterItem.user.stance
        holder.instagramNick.text = adapterItem.user.instagramNick

        val todaySpotHandler = object : TodaySpotView.TodaySpotViewHandler {
            override fun onBubbleClick() {
                adapterItem.user.todaySpot?.let { todaySpot ->
                    handler.onTodaySpotClick(todaySpot)
                }
            }
        }
        val todaySpotConfiguration = TodaySpotView.TodaySpotViewConfiguration(adapterItem.user.todaySpot?.spotName ?: "", todaySpotHandler)
        holder.todaySpot.configuration = todaySpotConfiguration
        holder.todaySpot.isVisible = adapterItem.user.todaySpot != null && SpotUtils.isTodaySpotValid(adapterItem.user.todaySpot)

        holder.binding.root.onClick {
            handler.onCrewMemberClick(adapterItem.user)
        }

        holder.instagramNick.isVisible = adapterItem.user.instagramNick != null
        holder.instagramIcon.isVisible = adapterItem.user.instagramNick != null

        holder.instagramNick.onClick {
            adapterItem.user.instagramNick?.let { nick ->
                handler.onInstagramClick(nick)
            }
        }

        handler.itemsLoaded()
    }

    class CrewMembersAdapterViewHolder(val binding: CrewMemberItemViewBinding) : BaseViewHolder(binding.root) {
        val profilePicture = binding.profilePictureIV
        val nick = binding.nickTV
        val realName = binding.nameTV
        val stance = binding.stanceTV
        val instagramNick = binding.instagramNickTV
        val todaySpot = binding.todaySpotV
        val instagramIcon = binding.instagramIV
    }

    data class CrewMembersAdapterItem(val user: User) : BaseAdapterItem()

    interface CrewMembersAdapterHandler {
        fun onTodaySpotClick(todaySpot: TodaySpot)
        fun onCrewMemberClick(member: User)
        fun onInstagramClick(instagramNick: String)
        fun itemsLoaded()
    }
}