package com.grove.crm.ui.browser

import android.os.Bundle
import com.grove.crm.R
import com.grove.crm.utils.BaseActivity

class BrowserActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        val title = intent.getStringExtra(TITLE)
        val url = intent.getStringExtra(URL)
        val content = intent.getStringExtra(CONTENT)
        this.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        BrowserFragment.newInstance(url, content)?.let {fragment ->
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.browser_container, fragment)
                commitAllowingStateLoss()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val TITLE = "title"
        const val URL = "url"
        const val CONTENT = "content"
    }
}