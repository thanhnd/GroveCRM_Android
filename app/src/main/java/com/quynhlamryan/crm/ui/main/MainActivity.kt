package com.quynhlamryan.crm.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Account
import com.quynhlamryan.crm.data.model.Article
import com.quynhlamryan.crm.data.model.Promotion
import com.quynhlamryan.crm.ui.browser.BrowserActivity
import com.quynhlamryan.crm.ui.inputPhone.InputPhoneActivity
import com.quynhlamryan.crm.ui.mapstore.MapStoreActivity
import com.quynhlamryan.crm.ui.membercard.MemberCardActivity
import com.quynhlamryan.crm.ui.setting.SettingActivity
import com.quynhlamryan.crm.ui.transaction.TransactionActivity
import com.quynhlamryan.crm.utils.AccountManager
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private var articleSection: ArticleSection? = null
    private var promotionSection: PromotionSection? = null
    private var accountSection: AccountSection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        sectionAdapter = SectionedRecyclerViewAdapter()

        accountSection = AccountSection(
            object : AccountSection.ClickListener {
                override fun onMemberCardClicked() {
                    openMemberCard()
                }

                override fun onTransactionHistoryClicked() {
                    openTransactionHistory()
                }
            })
        sectionAdapter.addSection(accountSection)

        //Promotion section
        promotionSection = PromotionSection(
            resources.getString(R.string.promotion),
            object : PromotionSection.ClickListener {
                override fun onItemRootViewClicked(
                    section: PromotionSection?,
                    promotion: Promotion
                ) {
                    openBrowser(promotion)
                }
            })


        sectionAdapter.addSection(promotionSection)

        //Article section
        articleSection = ArticleSection(
            resources.getString(R.string.news),
            object : ArticleSection.ClickListener {
                override fun onItemRootViewClicked(
                    section: ArticleSection?,
                    article: Article
                ) {
                    openBrowser(article)
                }
            })

        sectionAdapter.addSection(articleSection)
        rvMain.adapter = sectionAdapter

        ivAvatar.setOnClickListener {
            AccountManager.account?.let {
                openSetting()
            } ?: run {
                AccountManager.logout()
                val intent = Intent(applicationContext, InputPhoneActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }

    private fun openSetting() {
        Intent(this, SettingActivity::class.java)
            .apply {
                startActivity(this)
            }
    }

    override fun onStart() {
        super.onStart()

        mainActivityViewModel.getArticles()?.observe(this, Observer { articles ->
            articleSection?.apply {
                setList(articles)
                sectionAdapter.notifyDataSetChanged()
            }
        })

        mainActivityViewModel.getPromotions()?.observe(this, Observer { promotions ->
            promotionSection?.apply {
                setList(promotions)
                sectionAdapter.notifyDataSetChanged()
            }
        })

        AccountManager.account?.apply {
            bindAccount(this)
        } ?: run {
            AccountManager.token?.let {
                mainActivityViewModel.getAccount()?.observe(this, Observer { account ->
                    accountSection?.apply {
                        account?.let { account ->
                            bindAccount(account)
                        }
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()

        AccountManager.account?.let { account ->
            bindAccount(account)
        }
    }

    private fun bindAccount(account: Account) {
        AccountManager.account = account
        accountSection?.setAccount(account)
        sectionAdapter.notifyDataSetChanged()

        Glide
            .with(this@MainActivity)
            .load(account.urlAvatar)
            .circleCrop()
            .into(ivAvatar)
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

    private fun openBrowser(promotion: Promotion) {
        Intent(this, BrowserActivity::class.java)
            .apply {
                putExtra(BrowserActivity.TITLE, promotion.title)
                putExtra(BrowserActivity.URL, promotion.url)
                putExtra(BrowserActivity.CONTENT, promotion.content)
                startActivity(this)
            }
    }

    private fun openMemberCard() {
        Intent(this, MemberCardActivity::class.java)
            .apply {
                startActivity(this)
            }
    }

    private fun openTransactionHistory() {
        Intent(this, TransactionActivity::class.java)
            .apply {
                startActivity(this)
            }
    }
}