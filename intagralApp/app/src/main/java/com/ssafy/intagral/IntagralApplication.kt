package com.ssafy.intagral

import android.app.Application
import com.ssafy.intagral.util.PreferenceUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IntagralApplication: Application() {

    companion object{
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
    }
}