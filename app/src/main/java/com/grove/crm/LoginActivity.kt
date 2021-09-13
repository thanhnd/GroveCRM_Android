
package com.grove.crm

import android.content.Intent
import android.os.Bundle
import com.grove.crm.ui.inputPhone.InputPhoneActivity
import com.grove.crm.ui.main.MainActivity
import com.grove.crm.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
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
            finish()
        }
    }
}