package com.g3.spot_guide.screens.todaySpot.addTodaySpot

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.g3.base.screens.dialogs.BaseBottomSheet
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.AddTodaySpotBottomSheetFragmentBinding
import com.g3.spot_guide.extensions.afterTextChanged
import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.TodaySpot
import com.g3.spot_guide.utils.SpotUtils
import com.g3.spot_guide.views.BottomButtonView
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.Serializable

data class AddTodaySpotBottomSheetFragmentArguments(
    val spot: Spot,
    val time: String? = null
) : Serializable

class AddTodaySpotBottomSheetFragment : BaseBottomSheet<AddTodaySpotBottomSheetFragmentBinding, AddTodaySpotBottomSheetFragmentHandler>() {

    private val arguments: AddTodaySpotBottomSheetFragmentArgs by navArgs()

    private val addTodaySpotBottomSheetFragmentViewModel: AddTodaySpotBottomSheetFragmentViewModel by viewModel()
    override fun setBinding(layoutInflater: LayoutInflater) = AddTodaySpotBottomSheetFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: AddTodaySpotBottomSheetFragmentBinding, context: Context) {
        binding.spotNameTV.text = arguments.args.spot.name

        setupTimeListeners()
        setupAddButton()
        setupExistingTime()
    }

    private fun setupTimeListeners() {
        binding.hoursET.afterTextChanged { hours ->
            if (hours.isNotBlank() && hours.isNotEmpty()) {
                if (hours.toInt() > 23) {
                    addTodaySpotBottomSheetFragmentViewModel.hours = "23"
                    binding.hoursET.setText("23")
                } else {
                    addTodaySpotBottomSheetFragmentViewModel.hours = hours
                }
            }
        }

        binding.minutesET.afterTextChanged { minutes ->
            if (minutes.isNotBlank() && minutes.isNotEmpty()) {
                if (minutes.toInt() > 59) {
                    addTodaySpotBottomSheetFragmentViewModel.minutes = "59"
                    binding.minutesET.setText("59")
                } else {
                    addTodaySpotBottomSheetFragmentViewModel.minutes = minutes
                }
            }
        }
    }

    private fun setupAddButton() {
        val handler = object : BottomButtonView.BottomButtonViewHandler {
            override fun onButtonClick() {
                addTodaySpotBottomSheetFragmentViewModel.todaySpotAdded.observe(this@AddTodaySpotBottomSheetFragment, { addedEither ->
                    val result = addedEither.getValueOrNull()
                    if (result != null) {
                        handler.saveTodaySpot(result)
                        dismiss()
                    } else {
                        showSnackBar(binding.root, R.string.error__today_spot_not_saved)
                        binding.loadingV.isVisible = false
                    }
                })

                binding.loadingV.isBlurVisible = true
                binding.loadingV.isVisible = true

                val time = "${addTodaySpotBottomSheetFragmentViewModel.hours}:${addTodaySpotBottomSheetFragmentViewModel.minutes}"
                val newTodaySpot = TodaySpot(arguments.args.spot.id, arguments.args.spot.name, SpotUtils.getTodayDate(), time)
                addTodaySpotBottomSheetFragmentViewModel.addTodaySpotToCurrentUser(newTodaySpot)
            }
        }
        binding.addTodaySpotB.configuration = BottomButtonView.BottomButtonViewConfiguration(R.string.today_spot__add, handler)
    }

    private fun setupExistingTime() {
        arguments.args.time?.let { time ->
            val separatorIndex = time.indexOf(":")
            val hours = time.substring(0, separatorIndex)
            val minutes = time.subSequence(separatorIndex + 1, time.length)

            binding.hoursET.setText(hours)
            binding.minutesET.setText(minutes)
        }
    }

}

interface AddTodaySpotBottomSheetFragmentHandler : BaseFragmentHandler {
    fun saveTodaySpot(newTodaySpot: TodaySpot)
}