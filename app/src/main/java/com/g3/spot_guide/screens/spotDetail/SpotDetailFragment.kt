package com.g3.spot_guide.screens.spotDetail

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.SpotDetailFragmentBinding
import com.g3.spot_guide.screens.EmptyViewModel

class SpotDetailFragment : BaseFragment<SpotDetailFragmentBinding, EmptyViewModel, SpotDetailFragmentHandler>() {

    override val viewModel: EmptyViewModel by viewModels { EmptyViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): SpotDetailFragmentBinding = SpotDetailFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: SpotDetailFragmentBinding, context: Context) {

    }
}

interface SpotDetailFragmentHandler : BaseFragmentHandler {

}