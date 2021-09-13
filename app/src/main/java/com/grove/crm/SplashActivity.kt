package com.grove.crm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.grove.crm.ui.main.MainActivity
import com.grove.crm.utils.AccountManager


class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(Runnable { // This method will be executed once the timer is over
            // Start your app main activity
            val intent = AccountManager.token?.let {
                Intent(this, MainActivity::class.java)
            } ?: run {
                Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}