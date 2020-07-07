package com.g3.spot_guide.screens.test

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.g3.spot_guide.base.BaseFragment
import com.g3.spot_guide.base.BaseFragmentHandler
import com.g3.spot_guide.databinding.TestFragmentBinding
import com.g3.spot_guide.extensions.onClick

class TestFragment : BaseFragment<TestFragmentBinding, TestFragmentViewModel, TestFragmentHandler>() {

    override val viewModel: TestFragmentViewModel by viewModels { TestFragmentViewModel.ViewModelInstanceFactory(this) }
    override fun setBinding(layoutInflater: LayoutInflater): TestFragmentBinding = TestFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: TestFragmentBinding, context: Context) {
        binding.testTV.text = viewModel.testText

        binding.testB.onClick {
            viewModel.testText = "Changed text"
            binding.testTV.text = viewModel.testText
        }
    }
}

interface TestFragmentHandler : BaseFragmentHandler