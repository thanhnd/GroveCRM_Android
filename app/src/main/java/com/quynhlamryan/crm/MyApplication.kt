package com.quynhlamryan.crm

import androidx.multidex.MultiDexApplication

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        var instance: MyApplication? = null
    }
}