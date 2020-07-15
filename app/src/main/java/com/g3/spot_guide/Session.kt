package com.g3.spot_guide

import android.app.Application
import android.content.ContextWrapper
import com.g3.spot_guide.routing.SpotGuideCoordinator
import com.pixplicity.easyprefs.library.Prefs
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
        initPrefs()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Session)
            modules(appModules)
        }
    }

    private fun initPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}