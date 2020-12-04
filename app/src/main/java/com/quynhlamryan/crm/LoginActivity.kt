package com.quynhlamryan.crm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quynhlamryan.crm.ui.inputPhone.InputPhoneActivity
import com.quynhlamryan.crm.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity , InputPhoneActivity::class.java)
            startActivity(intent)
        }

        btnSkip.setOnClickListener {
            val intent = Intent(this@LoginActivity , MainActivity::class.java)
            startActivity(intent)
        }
    }
}