package com.quynhlamryan.crm.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.ivAvatar
import kotlinx.android.synthetic.main.activity_setting.*

class ProfileActivity : AppCompatActivity() {
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setTitle(R.string.profile_info)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        btnDone.setOnClickListener {
            profileViewModel.getListTransactions()?.observe(this, Observer { config ->
                AccountManager.config = config
                tvHotLine.text = getString(R.string.hot_line, (config.hotline ?: ""))
            })
        }

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
            edtEmail.setText(phoneNumber)
        }
    }
}