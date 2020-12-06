package com.quynhlamryan.crm.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.LoginActivity
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.ui.browser.BrowserActivity
import com.quynhlamryan.crm.ui.profile.ProfileActivity
import com.quynhlamryan.crm.ui.transaction.TransactionActivity
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitle(R.string.setting)

        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        cvTransaction.setOnClickListener {
            Intent(this, TransactionActivity::class.java)
                .apply {
                    startActivity(this)
                }
        }

        cvProfile.setOnClickListener {
            Intent(this, ProfileActivity::class.java)
                .apply {
                    startActivity(this)
                }
        }

        tvAboutUs.setOnClickListener {
            val config = AccountManager.config ?: return@setOnClickListener
            Intent(this, BrowserActivity::class.java)
                .apply {
                    putExtra(BrowserActivity.TITLE, getString(R.string.about_us))
                    putExtra(BrowserActivity.URL, config.aboutUs)
                    startActivity(this)
                }
        }

        tvPolicy.setOnClickListener {
            val config = AccountManager.config ?: return@setOnClickListener
            Intent(this, BrowserActivity::class.java)
                .apply {
                    putExtra(BrowserActivity.TITLE, getString(R.string.policy))
                    putExtra(BrowserActivity.URL, config.termOfUse)
                    startActivity(this)
                }
        }

        btnLogOut.setOnClickListener {
            AccountManager.logout()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        AccountManager.account?.let { account ->
            Glide
                .with(this)
                .load(account.urlAvatar)
                .circleCrop()
                .into(ivAvatar)
            tvAccountName.text = account.fullName
            tvPhone.text = account.phoneNumber
            tvMemberType.text = account.typeMember
            tvScore.text = getString(R.string.score, "${account.total ?: 0}")
        }

        settingViewModel.getListTransactions()?.observe(this, Observer { config ->
            AccountManager.config = config
            tvHotLine.text = getString(R.string.hot_line, (config.hotline ?: ""))
        })
    }
}