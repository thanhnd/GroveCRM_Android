package com.quynhlamryan.crm.ui.inputPhone

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.ui.OtpActivity
import kotlinx.android.synthetic.main.activity_input_phone.*

class InputPhoneActivity : AppCompatActivity() {

    private lateinit var inputPhoneViewModel: InputPhoneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_input_phone)
        title = getString(R.string.title_activity_input_phone)

        inputPhoneViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(InputPhoneViewModel::class.java)

        inputPhoneViewModel.inputPhoneNumberFormState.observe(this@InputPhoneActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both edtPhone / password is valid
            btnNext.isEnabled = loginState.isDataValid
        })

        inputPhoneViewModel.loginResult.observe(this@InputPhoneActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            } else if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                val intent = Intent(this, OtpActivity::class.java)
                startActivity(intent)

                finish()
            }
        })

        edtPhone.afterTextChanged {
            inputPhoneViewModel.loginDataChanged(
                edtPhone.text.toString()
            )
        }

        edtPhone.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    inputPhoneViewModel.inputPhone(
                        edtPhone.text.toString()
                    )
            }
            false
        }

        btnNext.setOnClickListener {
            loading.visibility = View.VISIBLE
            inputPhoneViewModel.inputPhone(edtPhone.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
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