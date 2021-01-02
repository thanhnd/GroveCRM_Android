package com.quynhlamryan.crm.ui.browser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quynhlamryan.crm.R

class BrowserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        val title = intent.getStringExtra(TITLE)
        val url = intent.getStringExtra(URL)
        val content = intent.getStringExtra(CONTENT)
        this.title = title

        BrowserFragment.newInstance(url, content)?.let {fragment ->
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.browser_container, fragment)
                commitAllowingStateLoss()
            }
        }
    }

    companion object {
        const val TITLE = "title"
        const val URL = "url"
        const val CONTENT = "content"
    }
}