package com.quynhlamryan.crm.ui.profile

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    lateinit var profileViewModel: ProfileViewModel
    private var name: String? = null
    private var birthday: String? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setTitle(R.string.profile_info)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btnDone.setOnClickListener {
            if (!isValidInput()) {
                return@setOnClickListener
            }
            profileViewModel.updateAccount(name, birthday, email)
                ?.observe(this, Observer { isSuccess ->
                    if (isSuccess) {
                        finish()
                    }
                })
        }

        edtFullName.addTextChangedListener {
            name = it?.toString()
        }

        edtBirthday.addTextChangedListener {
            birthday = it?.toString()
        }

        edtEmail.addTextChangedListener {
            email = it?.toString()
        }
    }

    private fun isValidInput(): Boolean {
        var result = true
        AccountManager.account?.let { account->
            if (!account.fullName.isNullOrEmpty()) {
                if (name.isNullOrEmpty()) {
                    edtFullName.error = getString(R.string.error_empty_name)
                    result = false
                } else if (name!!.length > 100) {
                    edtFullName.error = getString(R.string.error_name_too_long)
                    result = false
                }
            }

            if (!account.email.isNullOrEmpty()) {
                if (email.isNullOrEmpty()) {
                    edtEmail.error = getString(R.string.error_empty_email)
                    result = false
                } else if (email!!.length > 100) {
                    edtEmail.error = getString(R.string.error_email_too_long)
                    result = false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.error = getString(R.string.error_email_invalid)
                    result = false
                }
            }

            if (!account.dob.isNullOrEmpty()) {
                if (birthday.isNullOrEmpty()) {
                    edtBirthday.error = getString(R.string.error_empty_birthday)
                    result = false
                }
            }
        }


        return result
    }

    override fun onStart() {
        super.onStart()

        AccountManager.account?.apply {
            Glide
                .with(this@ProfileActivity)
                .load(urlAvatar)
                .circleCrop()
                .into(ivAvatar)
            edtFullName.setText(fullName)
            edtBirthday.setText(dob)
            edtPhone.setText(phoneNumber)
            edtEmail.setText(email)
        }
    }
}