package com.g3.spot_guide.screens.onBoarding

import android.content.Context
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.g3.base.screens.fragment.BaseFragment
import com.g3.base.screens.fragment.BaseFragmentHandler
import com.g3.spot_guide.R
import com.g3.spot_guide.databinding.OnBoardingFragmentBinding
import com.g3.spot_guide.extensions.onClick
import com.g3.spot_guide.views.AppBarView

class OnBoardingFragment : BaseFragment<OnBoardingFragmentBinding, OnBoardingFragmentHandler>() {

    private val fragments: List<OnBoardingPagerFragment> by lazy {
        mutableListOf(
            OnBoardingPagerFragment(R.drawable.on_boarding_test_1, R.string.on_boarding__community_spots),
            OnBoardingPagerFragment(R.drawable.on_boarding_test_2, R.string.on_boarding__add_review),
            OnBoardingPagerFragment(R.drawable.on_boarding_test_3, R.string.on_boarding__share_tricks))
    }

    private val adapter: FragmentStatePagerAdapter by lazy {
        object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = fragments[position]
            override fun getCount() = fragments.size
        }
    }

    override fun setBinding(layoutInflater: LayoutInflater): OnBoardingFragmentBinding = OnBoardingFragmentBinding.inflate(layoutInflater)
    override fun onFragmentLoadingFinished(binding: OnBoardingFragmentBinding, context: Context) {
        setupViewPager()
        setupNextButton()
        setupAppBar()
    }

    private fun setupViewPager() {
        binding.onBoardingVP.adapter = adapter
        binding.onBoardingVP.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                binding.nextB.isVisible = position == fragments.size - 1
            }
        })

        binding.indicatorsV.setViewPager(binding.onBoardingVP)
    }

    private fun setupNextButton() {
        binding.nextB.onClick { handler.onNextClick() }
    }

    private fun setupAppBar() {
        binding.appBarV.configuration = AppBarView.AppBarViewConfiguration(R.string.app_name, false, null, null)
    }
}

interface OnBoardingFragmentHandler : BaseFragmentHandler {
    fun onNextClick()
}