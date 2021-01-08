package com.quynhlamryan.crm.ui.browser

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.fragment_browser.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class BrowserFragment : Fragment() {

    private var url: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(URL)
        content = arguments?.getString(CONTENT)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                CustomProgressDialog.showProgressDialog(context)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                CustomProgressDialog.dismissProgressDialog()
                super.onPageFinished(view, url)
            }
        }

        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
        } else if (!content.isNullOrEmpty()) {
            webView.loadData(content, "text/html; charset=utf-8", "utf-8");
        }
    }

    companion object {
        const val URL = "url"
        const val CONTENT = "content"

        fun newInstance(url: String?, content: String?): BrowserFragment? {
            val fragment = BrowserFragment()
            Bundle().apply {
                putString(URL, url)
                putString(CONTENT, content)
                fragment.arguments = this
            }

            return fragment
        }
    }
}