package com.grove.crm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.grove.crm.Constants.PREF_NAME
import com.grove.crm.Constants.PRIVATE_MODE
import java.util.*

class LocaleManager(private val context: Context) {
    private val prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    fun setLocale(c: Context): Context {

        return language?.let { updateResources(c, it) } ?: c
    }

    fun setNewLocale(c: Context, language: String): Context {
        persistLanguage(language)
        return updateResources(c, language)
    }

    private val language: String?

        get() = prefs?.getString(LANGUAGE_KEY, getLocale(context.resources).language)

    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(language: String) {
        // use commit() instead of apply(), because sometimes we kill the application process
        // immediately that prevents apply() from finishing
        prefs?.edit()?.putString(LANGUAGE_KEY, language)?.commit()
    }

    private fun updateResources(context: Context, language: String): Context {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            setLocaleForApi24(config, locale)
            context = context.createConfigurationContext(config)
        } else if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }

    @RequiresApi(api = VERSION_CODES.N)
    private fun setLocaleForApi24(config: Configuration, target: Locale) {
        val set: MutableSet<Locale> = LinkedHashSet()
        // bring the target locale to the front of the list
        set.add(target)
        val all = LocaleList.getDefault()
        for (i in 0 until all.size()) {
            // append other locales supported by the user
            set.add(all[i])
        }
        val locales = set.toTypedArray()
        config.setLocales(LocaleList(*locales))
    }

    companion object {
        const val LANGUAGE_VI = "vi"
        const val LANGUAGE_KEY = "language_key"
        fun getLocale(res: Resources): Locale {
            val config = res.configuration
            return if (Build.VERSION.SDK_INT >= VERSION_CODES.N) config.locales[0] else config.locale
        }
    }

}