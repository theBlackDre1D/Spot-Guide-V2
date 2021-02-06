package com.g3.spot_guide.screens.spotDetail

import GeoCoderUtils
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.g3.base.either.Either
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.Session
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import com.g3.spot_guide.eventBus.EventBus
import com.g3.spot_guide.eventBus.EventBusListener
import com.g3.spot_guide.extensions.loadImageFromFirebase
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.models.User
import com.g3.spot_guide.views.BottomButtonView
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

data class SpotDetailFragmentArguments(
    val spot: Spot?,
    val spotId: String?
) : Serializable

open class SpotDetailFragment : BaseBottomSheet<SpotDetailFragmentBinding, SpotDetailFragmentHandler>(), SpotDetailPhotosAdapter.SpotDetailPhotosAdapterHandler,
    EventBusListener {

    private val arguments: SpotDetailFragmentArgs by navArgs()

    private val photosAdapter: SpotDetailPhotosAdapter by lazy { SpotDetailPhotosAdapter(this) }
    private val reviewsAdapter: SpotReviewsAdapter by lazy { SpotReviewsAdapter() }

    private val spotDetailFragmentViewModel: SpotDetailFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {
        setupObservers()
        setupSpotData()
        setupImagesRV()
        downloadImages()
        setupButtons()
        setupTodaySpot()
        setupReviews()

        binding.groundQualityPurpleTV.text = arguments.spotArguments.spot?.groundType

        EventBus.subscribeOnEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.unsubscribe(this)
    }

    private fun setupReviews() {
        binding.reviewsRV.adapter = reviewsAdapter

        spotDetailFragmentViewModel.spotReviews.observe(this, { reviewsEither ->
            binding.reviewsLoadingV.isVisible = false
            val reviews = reviewsEither.getValueOrNull()
            reviews?.let { reviews ->
                val adapterItems = mutableListOf<SpotReviewsAdapter.SpotReviewsAdapterItem>()
                reviews.forEach { spotReview ->
                    adapterItems.add(SpotReviewsAdapter.SpotReviewsAdapterItem(spotReview))
                }

                reviewsAdapter.injectData(adapterItems)
            }
        })
    }

    private fun setupSpotData() {
        val spot = arguments.spotArguments.spot

        if (spot != null) {
            spotDetailFragmentViewModel.spot.postValue(Either.Success(spot))
        } else {
            arguments.spotArguments.spotId?.let { spotId ->
                spotDetailFragmentViewModel.getSpot(spotId)
            }
        }
    }

    private fun setupObservers() {
        spotDetailFragmentViewModel.spot.observe(this, { spotEither ->
            val spotValue = spotEither.getValueOrNull()
            if (spotValue != null) {
                downloadImages()
                binding.spotNameTV.text = spotValue.name
                binding.spotLocationTV.text = GeoCoderUtils.getNameFromLocation(requireContext(), spotValue.location)
                binding.descriptionContentTV.text = spotValue.description
                binding.spotTypeTV.text = spotValue.spotType

                // TODO uncomment if current user cannot add spot review
//                binding.addReviewB.isVisible = spotValue.authorId != Session.loggedInUser?.id

                setupCrewMembersForThisSpot(spotValue.id)
                spotDetailFragmentViewModel.getSpotReviews(spotValue.id)
                spotDetailFragmentViewModel.getSpotAuthor(spotValue.authorId)
            } else {
                showSnackBar(binding.root, R.string.error__spots_load)
            }
        })

        spotDetailFragmentViewModel.imagesUris.observe(this, { imageUris ->
            val adapterItems = mutableListOf<SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem>()
            imageUris.forEach {
                adapterItems.add(SpotDetailPhotosAdapter.SpotDetailPhotosAdapterItem(it))
            }
            photosAdapter.injectData(adapterItems)
            binding.photosLoadingV.isVisible = imageUris.isEmpty()
        })

        spotDetailFragmentViewModel.spotAuthor.observe(this, { eitherUser ->
            eitherUser.getValueOrNull()?.let { author ->
                setupSpotAuthor(author)
            }
        })
    }

    private fun setupSpotAuthor(spotAuthor: User) {
        binding.spotAuthorProfilePictureIV.loadImageFromFirebase(spotAuthor.profilePictureUrl)
        binding.spotAuthorNameTV.text = spotAuthor.nick
    }

    private fun setupImagesRV() {
        val layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false)
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        binding.photosRV.adapter = photosAdapter
        binding.photosRV.layoutManager = layoutManager
        binding.photosRV.setHasFixedSize(true)
        binding.photosRV.addOnScrollListener(CenterScrollListener())
    }

    private fun downloadImages() {
        if (spotDetailFragmentViewModel.imagesUris.value?.isEmpty() == true) {
            val spot = spotDetailFragmentViewModel.spot.value?.getValueOrNull()
            spot?.let {
                spotDetailFragmentViewModel.loadImages(spot.images)
            }
        }
    }

    private fun setupButtons() {
        binding.navigateB.onClick {
            val spot = spotDetailFragmentViewModel.spot.value?.getValueOrNull()
            spot?.let { nonNullSpot ->
                handler.openSpotInMaps(nonNullSpot)
            }
        }

        binding.addReviewB.onClick {
            spotDetailFragmentViewModel.spot.value?.getValueOrNull()?.let { spot ->
                if (this is SpotDetailBottomSheet) {
                    this.dismiss()
                    handler.fromSpotDetailBottomSheetToAddSpotReview(spot)
                } else {
                    handler.fromSpotDetailToAddSpotReview(spot)
                }
            }
        }
    }

    private fun setupTodaySpot() {
        val buttonHandler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                val spot = spotDetailFragmentViewModel.spot.value?.getValueOrNull()
                spot?.let {
                    if (Session.loggedInUser?.todaySpot?.spotId == arguments.spotArguments.spot?.id) {
                        handler.fromSpotDetailToAddTodaySpot(spot, Session.loggedInUser?.todaySpot?.time)
                    } else {
                        handler.fromSpotDetailToAddTodaySpot(spot, null)
                    }
                }
            }
        }

        val currentUser = Session.loggedInUser
        if (currentUser != null) {
            if (currentUser.todaySpot?.spotId == arguments.spotArguments.spot?.id) {
                val buttonText = String.format(getString(R.string.spot_detail__today_spot_with_time), currentUser.todaySpot?.time)
                binding.todaySpotV.stringConfiguration = BottomButtonView.BottomButtonViewStringConfiguration(buttonText, buttonHandler)
            } else {
                binding.todaySpotV.configuration = BottomButtonView.BottomButtonViewConfiguration(R.string.spot_detail__today_spot, buttonHandler)
            }

            val liveData = handler.getTodaySpotLiveData()
            liveData.observe(this, { todaySpot ->
                val buttonText = String.format(getString(R.string.spot_detail__today_spot_with_time), todaySpot.time)
                binding.todaySpotV.stringConfiguration = BottomButtonView.BottomButtonViewStringConfiguration(buttonText, buttonHandler)
            })
        }
    }

    private fun setupCrewMembersForThisSpot(spotId: String) {
        spotDetailFragmentViewModel.spotCrewMembers.observe(this, { usersEither ->
            binding.spotCrewMembersLoadingV.isVisible = false
            usersEither.getValueOrNull()?.let { users ->
                binding.spotCrewMembersTV.isVisible = users.isNotEmpty()
                binding.spotCrewMemberContainerCL.isVisible = users.isNotEmpty()

                binding.spotCrewMemberContainerCL.onClick {
                    handler.fromSpotDetailToSpotCrewMembers(users)
                }
            }
        })

        spotDetailFragmentViewModel.getCrewMembersForThisSpot(spotId)
    }

    override fun onPhotoClick(position: Int) {
        handler.openImagesGallery(spotDetailFragmentViewModel.imagesUris.value ?: listOf(), position)
    }

    override fun onEventPosted(event: EventBus.Event) {
        if (event is EventBus.Event.SpotReviewAdded) {
            val newReview = event.newReview
            newReview.user = Session.loggedInUser
            val adapterItem = SpotReviewsAdapter.SpotReviewsAdapterItem(newReview)
            reviewsAdapter.addReview(adapterItem)
        }
    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler {
    fun openImagesGallery(images: List<Uri>, position: Int)
    fun openSpotInMaps(spot: Spot)
    fun openAddReviewBottomSheet(spot: Spot)
    fun fromSpotDetailToAddTodaySpot(spot: Spot, time: String?)
    fun getTodaySpotLiveData(): LiveData<TodaySpot>
    fun fromSpotDetailToSpotCrewMembers(spotCrewMembers: List<User>)
    fun fromSpotDetailToAddSpotReview(spot: Spot)
    fun fromSpotDetailBottomSheetToAddSpotReview(spot: Spot)
}