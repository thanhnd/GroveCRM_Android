package com.quynhlamryan.crm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.ui.browser.BrowserActivity
import com.quynhlamryan.crm.ui.mapstore.MapStoreActivity
import com.quynhlamryan.crm.utils.Logger
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    private val adapter = MainAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        adapter.onItemClick = { article ->
            Logger.d(article.title)
            openBrowser(article)
        }
        rvMain.adapter = adapter

    }

    override fun onStart() {
        super.onStart()

        mainActivityViewModel.getArticles()!!.observe(this, Observer { articles ->
            adapter.articles = articles
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_location -> openMapStore()

            else -> {
            }
        }
        return true
    }

    private fun openMapStore() {
        Intent(this, MapStoreActivity::class.java)
            .apply {
                startActivity(this)
            }
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
}