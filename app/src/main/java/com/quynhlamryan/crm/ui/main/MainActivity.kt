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
import com.quynhlamryan.crm.ui.main.ArticleSection.ClickListener
import com.quynhlamryan.crm.ui.mapstore.MapStoreActivity
import com.quynhlamryan.crm.ui.membercard.MemberCardActivity
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private var articleSection: ArticleSection? = null
    private var accountSection: AccountSection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        sectionAdapter = SectionedRecyclerViewAdapter()

        accountSection = AccountSection(
            object : AccountSection.ClickListener {
                override fun onMemberCardClicked() {
                    openMemberCard()
                }

                override fun onTransactionHistoryClicked() {
                    TODO("Not yet implemented")
                }

            })
        sectionAdapter.addSection(accountSection)

        articleSection = ArticleSection(
            resources.getString(R.string.news),
            object : ClickListener {
                override fun onItemRootViewClicked(
                    section: ArticleSection?,
                    article: Article
                ) {
                    openBrowser(article)
                }
            })

        sectionAdapter.addSection(articleSection)
        rvMain.adapter = sectionAdapter
    }

    override fun onStart() {
        super.onStart()

        mainActivityViewModel.getArticles()?.observe(this, Observer { articles ->
            articleSection?.apply {
                setList(articles)
                sectionAdapter.notifyDataSetChanged()
            }
        })

        mainActivityViewModel.getAccount()?.observe(this, Observer { account ->
            accountSection?.apply {
                account?.let { account->
                    setList(account)
                    sectionAdapter.notifyDataSetChanged()
                }

            }
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

    private fun openMemberCard() {
        Intent(this, MemberCardActivity::class.java)
            .apply {
                startActivity(this)
            }
    }
}