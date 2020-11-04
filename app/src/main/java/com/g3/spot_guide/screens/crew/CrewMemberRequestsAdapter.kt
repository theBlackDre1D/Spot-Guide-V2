package com.g3.spot_guide.screens.crew

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.CrewMemberRequestItemBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.BottomButtonsView

class CrewMemberRequestsAdapter(
    private val handler: CrewMemberRequestsAdapterHandler
): BaseRecyclerViewAdapter<CrewMemberRequestsAdapter.CrewMemberRequestsAdapterViewHolder, CrewMemberRequestsAdapter.CrewMemberRequestsAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): CrewMemberRequestsAdapterViewHolder {
        return CrewMemberRequestsAdapterViewHolder(CrewMemberRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun bindAdapterViewHolder(holder: CrewMemberRequestsAdapterViewHolder, adapterItem: CrewMemberRequestsAdapterItem, position: Int) {
        holder.profilePicture.loadImageFromFirebase(adapterItem.user.profilePictureUrl)
        holder.nick.text = adapterItem.user.nick
        holder.name.text = adapterItem.user.fullName
        holder.stance.text = adapterItem.user.stance
        holder.instagramNick.text = adapterItem.user.instagramNick

        val buttonsHandler = object : BottomButtonsView.BottomButtonsViewListener {
            override fun onLeftButtonClick() {
                handler.onRequestDecision(false, adapterItem.user.id)
            }

            override fun onRightButtonClick() {
                handler.onRequestDecision(true, adapterItem.user.id)
            }

        }
        holder.buttons.configuration = BottomButtonsView.BottomButtonsViewConfiguration(R.string.crew_decline, R.string.crew__accept, buttonsHandler)

        holder.instagramNick.isVisible = adapterItem.user.instagramNick != null
        holder.instagramIcon.isVisible = adapterItem.user.instagramNick != null

        holder.instagramNick.onClick {
            adapterItem.user.instagramNick?.let { nick ->
                handler.onInstagramClick(nick)
            }
        }

        handler.itemsLoaded()
    }

    class CrewMemberRequestsAdapterViewHolder(val binding: CrewMemberRequestItemBinding) : BaseViewHolder(binding.root) {
        val profilePicture = binding.profilePictureIV
        val nick = binding.nickTV
        val name = binding.nameTV
        val stance = binding.stanceTV
        val instagramNick = binding.instagramNickTV
        val instagramIcon = binding.instagramIV
        val buttons = binding.buttonsV
    }

    class CrewMemberRequestsAdapterItem(val user: User) : BaseAdapterItem()

    interface CrewMemberRequestsAdapterHandler {
        fun onRequestDecision(accept: Boolean, userId: String)
        fun onInstagramClick(instagramNick: String)
        fun itemsLoaded()
    }
}