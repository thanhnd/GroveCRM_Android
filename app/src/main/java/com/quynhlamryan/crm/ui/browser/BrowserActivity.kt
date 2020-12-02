package com.quynhlamryan.crm.ui.browser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.quynhlamryan.crm.R

class BrowserActivity : AppCompatActivity() {


    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        val title = intent.getStringExtra(TITLE)
        val url = intent.getStringExtra(URL)
        val content = intent.getStringExtra(CONTENT)
        this.title = title
        webView = findViewById(R.id.webview)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        } else if (!content.isNullOrEmpty()) {
            webView.loadData(content, "text/html; charset=utf-8", "utf-8");
        }
    }

    companion object {
        const val TITLE = "title"
        const val URL = "url"
        const val CONTENT = "content"
    }
}