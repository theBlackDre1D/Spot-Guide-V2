package com.bluecrystal

import android.app.Application


class Session : Application() {

    companion object {
        lateinit var application: Session
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}