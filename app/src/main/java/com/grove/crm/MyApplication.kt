package com.grove.crm

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDexApplication
import com.grove.crm.utils.LocaleManager

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
//        Lingver.init(this, LocaleManager.getLocale(resources))
    }
    companion object {
        var instance: MyApplication? = null
        var localeManager: LocaleManager? = null
    }

    override fun attachBaseContext(base: Context) {
        localeManager = LocaleManager(base)
        super.attachBaseContext(localeManager?.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager?.setLocale(this)
    }
}