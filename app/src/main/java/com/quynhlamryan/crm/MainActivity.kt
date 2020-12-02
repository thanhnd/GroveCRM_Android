package com.quynhlamryan.crm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.ui.browser.BrowserActivity
import com.quynhlamryan.crm.ui.main.MainAdapter
import com.quynhlamryan.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    private val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        adapter.onItemClick = {article ->
            Logger.d(article.title)
            openBrowser(article)
        }
        rvMain.adapter = adapter

    }

    private fun openBrowser(article: Article) {
        Intent(this, BrowserActivity::class.java)
            .apply {
                putExtra(BrowserActivity.TITLE, article.title)
                putExtra(BrowserActivity.URL, article.url)
                putExtra(BrowserActivity.CONTENT, article.content)
                startActivity(this)
            }
    }

    override fun onStart() {
        super.onStart()

        mainActivityViewModel.getArticles()!!.observe(this, Observer { articles ->
            adapter.articles = articles
        })
    }
}