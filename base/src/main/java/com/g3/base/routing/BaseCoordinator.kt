package com.g3.base.routing

import android.app.Activity
import android.content.Intent

abstract class BaseCoordinator {

    protected fun startActivity(activity: Activity, intent: Intent) {
        this.startActivity(activity, intent, false)
    }

    protected fun startActivity(activity: Activity, intent: Intent, finishStarterActivity: Boolean = false) {
        if (finishStarterActivity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        }
        activity.startActivity(intent)
    }

    protected fun startActivityForResult(activity: Activity, intent: Intent, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }
}