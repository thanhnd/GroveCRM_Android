package com.grove.crm.utils

import android.R
import android.content.Context
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.grove.crm.MyApplication

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MyApplication.localeManager?.setLocale(base))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}