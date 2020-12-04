package com.quynhlamryan.crm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quynhlamryan.crm.ui.main.MainActivity
import com.quynhlamryan.crm.R
import kotlinx.android.synthetic.main.activity_otp.*

class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        btnNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}