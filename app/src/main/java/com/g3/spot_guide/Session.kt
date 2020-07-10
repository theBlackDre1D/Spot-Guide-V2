package com.g3.spot_guide

import android.app.Application
import com.g3.spot_guide.routing.SpotGuideCoordinator
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Session : Application() {

    companion object {
        lateinit var application: Session
            private set
    }

    val coordinator: SpotGuideCoordinator by lazy { SpotGuideCoordinator() }

    override fun onCreate() {
        super.onCreate()
        application = this

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Session)
            modules(appModules)
        }
    }
}