package com.g3.spot_guide

import android.app.Application
import android.content.ContextWrapper
import com.g3.spot_guide.models.User
import com.g3.spot_guide.routing.SpotGuideCoordinator
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val LOGGED_IN_USER__PREFS_KEY = "LOGGED_IN_USER__PREFS_KEY"

class Session : Application() {

    companion object {
        lateinit var application: Session
            private set

        var loggedInUser: User? = null
            private set

        fun saveAndSetLoggedInUser(user: User?) {
            loggedInUser = user
            Prefs.putString(LOGGED_IN_USER__PREFS_KEY, Gson().toJson(user))
        }
    }

    val coordinator: SpotGuideCoordinator by lazy { SpotGuideCoordinator() }

    override fun onCreate() {
        super.onCreate()
        application = this

        initKoin()
        initPrefs()
        loadLoggedInUser()
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

    private fun loadLoggedInUser() {
        val userJson = Prefs.getString(LOGGED_IN_USER__PREFS_KEY, "")
        try {
            val user = Gson().fromJson(userJson, User::class.java)
            Session.loggedInUser = user
        } catch (e: Exception) {}
    }
}