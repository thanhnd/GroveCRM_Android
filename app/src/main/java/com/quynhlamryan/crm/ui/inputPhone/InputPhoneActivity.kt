package com.quynhlamryan.crm.ui.inputPhone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.ui.OtpActivity
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_input_phone.*

class InputPhoneActivity : AppCompatActivity() {

    private lateinit var inputPhoneViewModel: InputPhoneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_input_phone)
        title = getString(R.string.title_activity_input_phone)

        inputPhoneViewModel = ViewModelProvider(this)
            .get(InputPhoneViewModel::class.java)

        edtPhone.afterTextChanged {
            btnNext.isEnabled = Patterns.PHONE.matcher(it).matches()
        }

        edtPhone.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    getOtp(
                        edtPhone.text.toString()
                    )
            }
            false
        }

        btnNext.setOnClickListener {
            getOtp(edtPhone.text.toString())
        }

        btnNext.isEnabled = Patterns.PHONE.matcher(edtPhone.text.toString()).matches()
    }

    private fun getOtp(phoneNumber: String) {
        inputPhoneViewModel.getOtp(phoneNumber)?.observe(this, {
            val accountCode = it ?: return@observe
            AccountManager.accountCode = accountCode

            val intent = Intent(this, OtpActivity::class.java)
            startActivity(intent)

            finish()
        })
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}