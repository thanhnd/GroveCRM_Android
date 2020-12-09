package com.quynhlamryan.crm.ui.setting

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.LoginActivity
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.ApiClient
import com.quynhlamryan.crm.data.model.Account
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

        ivAvatar.setOnClickListener {
            openProfile()
        }

        cvProfile.setOnClickListener {
            openProfile()

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
            ApiClient.lazyMgr.reset()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun openProfile() {
        Intent(this, ProfileActivity::class.java)
            .apply {
                startActivity(this)
            }
    }

    override fun onStart() {
        super.onStart()

        AccountManager.account?.let { account ->
            bindAccount(account)
        }

        settingViewModel.getListTransactions()?.observe(this, Observer { config ->
            AccountManager.config = config
            tvHotLine.text = getString(R.string.hot_line, (config.hotline ?: ""))
        })
    }

    override fun onResume() {
        super.onResume()
        settingViewModel.getAccount()?.observe(this, Observer { account ->
            account?.let {
                AccountManager.account = it
                bindAccount(it)
            }
        })
    }

    private fun bindAccount(account: Account) {
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
}