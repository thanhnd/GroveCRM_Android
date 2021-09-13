package com.grove.crm.ui.policy

import android.content.Intent
import android.os.Bundle
import com.grove.crm.R
import com.grove.crm.data.ApiClient
import com.grove.crm.ui.browser.BrowserFragment
import com.grove.crm.ui.profile.ProfileActivity
import com.grove.crm.ui.profile.ProfileActivity.Companion.CALLING_ACTIVITY
import com.grove.crm.utils.AccountManager
import com.grove.crm.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_policy.*

class PolicyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        val url = "https://member.grovegroup.net:9000/showcontent/termofuse"
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