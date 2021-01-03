package com.g3.spot_guide.screens.spotDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.g3.base.recyclerView.BaseAdapterItem
import com.g3.base.recyclerView.BaseRecyclerViewAdapter
import com.g3.base.recyclerView.BaseViewHolder
import com.g3.spot_guide.databinding.SpotReviewItemBinding
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.models.SpotReview

class SpotReviewsAdapter : BaseRecyclerViewAdapter<SpotReviewsAdapter.SpotReviewsAdapterViewHolder, SpotReviewsAdapter.SpotReviewsAdapterItem>() {

    override fun createAdapterViewHolder(parent: ViewGroup, viewType: Int): SpotReviewsAdapterViewHolder {
        return SpotReviewsAdapterViewHolder(SpotReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }

    override fun bindAdapterViewHolder(holder: SpotReviewsAdapterViewHolder, adapterItem: SpotReviewsAdapterItem, position: Int) {
        holder.profilePicture.loadImageFromFirebase(adapterItem.spotReview.user?.profilePictureUrl)
        holder.authorName.text = adapterItem.spotReview.user?.fullName
        holder.ratingBar.rating = adapterItem.spotReview.spotRating.toFloat()
        holder.reviewText.text = adapterItem.spotReview.text
        holder.reviewDate.text = adapterItem.spotReview.date
    }

    fun addReview(newReview: SpotReviewsAdapterItem) {
        adapterItems.add(newReview)
        notifyDataSetChanged()
    }

    class SpotReviewsAdapterItem(val spotReview: SpotReview) : BaseAdapterItem()
    class SpotReviewsAdapterViewHolder(val binding: SpotReviewItemBinding) : BaseViewHolder(binding.root) {
        val profilePicture = binding.reviewAuthorIV
        val authorName = binding.reviewAuthorTV
        val ratingBar = binding.starRatingV
        val reviewText = binding.spotReviewTV
        val reviewDate = binding.reviewDateTV
    }
}