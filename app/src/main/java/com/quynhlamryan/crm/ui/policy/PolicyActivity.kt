package com.quynhlamryan.crm.ui.policy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.ApiClient
import com.quynhlamryan.crm.ui.browser.BrowserFragment
import com.quynhlamryan.crm.ui.profile.ProfileActivity
import com.quynhlamryan.crm.ui.profile.ProfileActivity.Companion.CALLING_ACTIVITY
import com.quynhlamryan.crm.utils.AccountManager
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        val url = "http://crm.pmquynhlam.vn/home/termofuse"
        this.title = getString(R.string.policy)

        BrowserFragment.newInstance(url, null)?.let { fragment ->
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.browser_container, fragment)
                commitAllowingStateLoss()
            }
        }

        btnAgree.setOnClickListener {
            openProfileActivity()
        }

        btnSkip.setOnClickListener {
            AccountManager.logout()
            ApiClient.lazyMgr.reset()
            finish()
        }
    }

    private fun openProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(CALLING_ACTIVITY, PolicyActivity::class.java.simpleName)
        startActivity(intent)
        finish()
    }
}