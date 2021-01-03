package com.g3.spot_guide.screens.spotReview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.AddSpotReviewBottomSheetBinding
import com.g3.spot_guide.eventBus.EventBus
import com.g3.spot_guide.extensions.afterTextChanged
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.utils.DateUtils
import org.koin.android.viewmodel.ext.android.viewModel

class AddSpotReviewBottomSheet : BaseBottomSheet<AddSpotReviewBottomSheetBinding, AddSpotReviewBottomSheetHandler>() {

    private val arguments: AddSpotReviewBottomSheetArgs by navArgs()

    private val addSpotReviewViewModel: AddSpotReviewViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater) = AddSpotReviewBottomSheetBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: AddSpotReviewBottomSheetBinding, context: Context) {
        setupUser()
        setupAddReviewButton()
        setupUI()

        binding.spotNameTV.text = arguments.args.name
        binding.reviewDateTV.text = DateUtils.getTodayDate()
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.postEvent(EventBus.Event.SpotReviewDismissed(arguments.args))
    }

    private fun setupUser() {
        val currentUser = Session.loggedInUser
        currentUser?.let { user ->
            binding.reviewAuthorIV.loadImageFromFirebase(user.profilePictureUrl)
            binding.reviewAuthorTV.text = user.fullName
        }
    }

    private fun setupAddReviewButton() {
        binding.postReviewB.onClick {
            binding.loadingContainerCL.isVisible = true
            binding.loadingV.isBlurVisible = true
            binding.postReviewB.isVisible = false
            addSpotReviewViewModel.addReview(arguments.args.id)
        }
    }

    private fun setupUI() {
        binding.spotReviewET.setText(addSpotReviewViewModel.reviewText.value)
        binding.starRatingV.rating = addSpotReviewViewModel.spotRating.value ?: 0f

        binding.spotReviewET.afterTextChanged { newText ->
            addSpotReviewViewModel.reviewText.postValue(newText)
        }

        binding.starRatingV.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                addSpotReviewViewModel.spotRating.value = rating
            }
        }

        addSpotReviewViewModel.addSpotReviewResult.observe(this, { resultEither ->
            binding.loadingContainerCL.isVisible = false
            val value = resultEither.getValueOrNull()
            if (value != null) {
                EventBus.postEvent(EventBus.Event.SpotReviewAdded(value, arguments.args))
                this.dismiss()
            } else {
                binding.postReviewB.isVisible = true
                showSnackBar(binding.root, R.string.error__add_spot_review)
            }
        })
    }
}

interface AddSpotReviewBottomSheetHandler : BaseFragmentHandler