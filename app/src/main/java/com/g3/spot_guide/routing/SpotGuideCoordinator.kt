package com.g3.spot_guide.routing

import android.app.Activity
import android.content.Intent
import com.g3.base.routing.BaseCoordinator
import com.g3.spot_guide.screens.addSpot.AddSpotActivity
import com.g3.spot_guide.screens.login.LogInActivity
import com.g3.spot_guide.screens.map.MainActivity
import com.g3.spot_guide.screens.onBoarding.OnBoardingActivity
import com.g3.spot_guide.screens.profile.editProfile.EDIT_PROFILE_PARAMETERS__EXTRAS_KEY
import com.g3.spot_guide.screens.profile.editProfile.EditProfileActivity
import com.g3.spot_guide.screens.profile.otherUserProfile.OtherUserProfileActivity
import com.g3.spot_guide.screens.profile.otherUserProfile.USER_PARAMETERS__EXTRAS_KEY
import com.g3.spot_guide.screens.splash.SplashActivity

class SpotGuideCoordinator : BaseCoordinator() {

    fun startMapActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        super.startActivity(activity, intent, true)
    }

    fun startOnBoardingActivity(activity: Activity) {
        val intent = Intent(activity, OnBoardingActivity::class.java)
        super.startActivity(activity, intent, true)
    }

    fun startAddSpotActivity(activity: Activity, parameters: AddSpotActivity.Parameters) {
        val intent = Intent(activity, AddSpotActivity::class.java)
        intent.putExtra(AddSpotActivity.ADD_SPOT_PARAMETERS__EXTRAS_KEY, parameters)
        super.startActivity(activity, intent)
    }

    fun startLoginActivity(activity: Activity) {
        val intent = Intent(activity, LogInActivity::class.java)
        super.startActivity(activity, intent, true)
    }

    fun startEditProfileActivity(activity: Activity, parameters: EditProfileActivity.Parameters, finishPreviousActivity: Boolean, ) {
        val intent = Intent(activity, EditProfileActivity::class.java)
        intent.putExtra(EDIT_PROFILE_PARAMETERS__EXTRAS_KEY, parameters)
        super.startActivity(activity, intent, finishPreviousActivity)
    }

    fun startOtherUserProfileActivity(activity: Activity, parameters: OtherUserProfileActivity.Parameters) {
        val intent = Intent(activity, OtherUserProfileActivity::class.java)
        intent.putExtra(USER_PARAMETERS__EXTRAS_KEY, parameters)
        super.startActivity(activity, intent)
    }

    fun startSplashActivity(activity: Activity) {
        val intent = Intent(activity, SplashActivity::class.java)
        super.startActivity(activity, intent)
    }
}