package com.quynhlamryan.crm.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.quynhlamryan.crm.BuildConfig

object Logger {
    var TAG = "QuynhLamRyan"
    var isDebug: Boolean = BuildConfig.DEBUG
    fun d() {
        if (isDebug) {
            val t = Thread.currentThread().stackTrace[3]
            android.util.Log.d(
                TAG,
                t.className.substring(
                    t.className.lastIndexOf('.') + 1
                )
                        + "." + t.methodName
            )
        }
    }

    fun <T> d(msg: T?) {
        if (isDebug) {
            val t = Thread.currentThread().stackTrace[3]
            android.util.Log.d(
                TAG,
                (t.className.substring(
                    t.className.lastIndexOf('.') + 1
                )
                        + "." + t.methodName + " "
                        + (msg?.toString() ?: "NULL"))
            )
        }
    }

    fun d(msg: String?, tr: Throwable?) {
        if (isDebug) android.util.Log.d(TAG, msg, tr)
    }

    fun w(tr: Throwable) {
        if (isDebug) android.util.Log.w(TAG, tr.localizedMessage, tr)
    }

    fun w(msg: String?) {
        if (isDebug) android.util.Log.w(TAG, msg)
    }

    fun w(msg: String?, tr: Throwable?) {
        if (isDebug) android.util.Log.w(TAG, msg, tr)
    }

    fun e(tr: Throwable) {
        if (isDebug) {
            android.util.Log.e(TAG, tr.localizedMessage, tr)
        } else if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(tr)
        }
    }

    fun <T> e(msg: T?) {
        if (isDebug && msg != null) android.util.Log.e(TAG, msg.toString())
    }

    fun <T> e(msg: T?, tr: Throwable?) {
        if (isDebug && msg != null) android.util.Log.e(TAG, msg.toString(), tr)
    }


    fun i(logtag: String?, string: String?) {
        // TODO Auto-generated method stub
        if (isDebug) android.util.Log.i(logtag, string)
    }

    fun i(msg: String?) {
        if (isDebug) android.util.Log.i(TAG, msg)
    }
}