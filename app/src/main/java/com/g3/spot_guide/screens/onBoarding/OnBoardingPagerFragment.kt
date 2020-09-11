package com.g3.spot_guide.screens.onBoarding

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.databinding.OnBoardingPagerFragmentBinding

class OnBoardingPagerFragment(@DrawableRes val image: Int, @StringRes val text: Int) : BaseFragment<OnBoardingPagerFragmentBinding, OnBoardingPagerFragmentHandler>() {

    override fun setBinding(layoutInflater: LayoutInflater): OnBoardingPagerFragmentBinding = OnBoardingPagerFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: OnBoardingPagerFragmentBinding, context: Context) {
        binding.onBoardingIV.setImageResource(image)
        binding.onBoardingTV.setText(text)
    }
}

interface OnBoardingPagerFragmentHandler : BaseFragmentHandler