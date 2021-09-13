package com.grove.crm.ui.setting

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.grove.crm.LoginActivity
import com.grove.crm.MyApplication
import com.grove.crm.R
import com.grove.crm.data.ApiClient
import com.grove.crm.data.model.Account
import com.grove.crm.data.model.Language
import com.grove.crm.ui.browser.BrowserActivity
import com.grove.crm.ui.main.MainActivity
import com.grove.crm.ui.profile.ProfileActivity
import com.grove.crm.ui.transaction.TransactionActivity
import com.grove.crm.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlin.system.exitProcess


class SettingActivity : BaseActivity() {
    private var languages: List<Language>? = null
    lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitle(R.string.setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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

        languages = listOf<Language>(
            Language(code = "vi", name = getString(R.string.vietnamese)),
            Language(code = "en", name = getString(R.string.english))
        )

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
            Firebase.auth.signOut()
            ApiClient.lazyMgr.reset()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        tvContactUs.setOnClickListener {
            AccountManager.config?.support?.let {
                showAlertDialog(
                    title = getString(R.string.contact_us),
                    message = fromHtml(it)
                )
            }
        }

        LocaleManager.getLocale(resources).language.let { code ->
            languages?.firstOrNull { it.code ==  code}?.apply {
                tvCurrentLanguage.text = this.name
            }
        }

        tvCurrentLanguage.setOnClickListener {
            showChooseLanguageDialog()
        }
    }

    private fun showChooseLanguageDialog() {
        val languages = languages ?: return
        var dialog: AlertDialog? = null
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_language))

        val arrayAdapter = LanguageAdapter(this, languages) { language ->
            dialog?.dismiss()
            setNewLocale(language.code, true)
        }
        builder.setAdapter(arrayAdapter) { _, _ ->
        }

        dialog = builder.create()
        dialog.show()
    }

    private fun setNewLocale(language: String, restartProcess: Boolean): Boolean {
        MyApplication.localeManager?.setNewLocale(this, language)
        val i = Intent(this, MainActivity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        if (restartProcess) {
            exitProcess(0)
        } else {
            Toast.makeText(this, "Activity restarted", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    @Suppress("DEPRECATION")
    fun fromHtml(source: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
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

        CustomProgressDialog.showProgressDialog(this)
        settingViewModel.getListTransactions()?.observe(this, Observer { config ->
            CustomProgressDialog.dismissProgressDialog()
            AccountManager.config = config
            tvHotLine.text = getString(R.string.hot_line, fromHtml(config.hotline ?: ""))
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun bindAccount(account: Account) {
        Glide
            .with(this)
            .load(account.urlAvatar)
            .circleCrop()
            .into(ivAvatar)
        tvAccountName.text = account.fullName
        tvPhone.text = account.phoneNumber
        tvMemberType.text = account.crmLevelName
        tvScore.text = getString(R.string.score, "${account.total ?: 0}")
    }
}