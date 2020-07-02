package com.g3.spot_guide.base

import android.app.Activity
import android.content.Intent

abstract class BaseCoordinator {

    protected fun startActivity(activity: Activity, intent: Intent) {
        this.startActivity(activity, intent, false)
    }

    @Suppress("MemberVisibilityCanBePrivate", "SameParameterValue")
    protected fun startActivity(activity: Activity, intent: Intent, finishStarterActivity: Boolean = false) {
        activity.startActivity(intent)
        if (finishStarterActivity) {
            activity.finish()
        }
    }

    protected fun startActivityForResult(activity: Activity, intent: Intent, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }
}
