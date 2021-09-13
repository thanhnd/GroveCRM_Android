package com.grove.crm.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Account
import com.grove.crm.data.model.Article
import com.grove.crm.data.model.HotNews
import com.grove.crm.data.model.Promotion
import com.grove.crm.ui.browser.BrowserActivity
import com.grove.crm.ui.inputPhone.InputPhoneActivity
import com.grove.crm.ui.mapstore.MapStoreActivity
import com.grove.crm.ui.membercard.MemberCardActivity
import com.grove.crm.ui.notify.NotifyActivity
import com.grove.crm.ui.setting.SettingActivity
import com.grove.crm.ui.transaction.TransactionActivity
import com.grove.crm.utils.AccountManager
import com.grove.crm.utils.BaseActivity
import com.grove.crm.utils.Logger
import com.grove.crm.utils.htmlToString
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_hotnews.*

class MainActivity : BaseActivity() {
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
        rvMain.adapter = sectionAdapter


        mainActivityViewModel.getHotNew()?.observe(this, Observer { hotNews ->
            hotNews?.let {
                Logger.d(hotNews)
                openDialog(hotNews)
            }
        })
    }

    private fun openDialog(hotNews: HotNews) {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_hotnews)
            tvHotNewCaption.text = hotNews.title
            tvHotNewDescription.visibility = View.GONE
            ivHotNewsImage.visibility = View.GONE

            hotNews.hotNewImageUrl?.let { hotNewsImageUrl ->
                Glide
                    .with(this@MainActivity)
                    .load(hotNewsImageUrl)
                    .centerCrop()
                    .into(ivHotNewsImage)
                ivHotNewsImage.visibility = View.VISIBLE

            }

            hotNews.content?.let {
                tvHotNewDescription.text = hotNews.content.htmlToString()
                tvHotNewDescription.visibility = View.VISIBLE
            }

            ibClose.setOnClickListener {
                dismiss()
            }
            btnViewHotNewsDetails.setOnClickListener {
                openBrowser(hotNews)
            }
            show()
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
            R.id.menu_notify -> openInbox()

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

    private fun openInbox() {
        Intent(this, NotifyActivity::class.java)
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

    private fun openBrowser(hotNews: HotNews) {
        Intent(this, BrowserActivity::class.java)
            .apply {
                putExtra(BrowserActivity.TITLE, hotNews.title)
                putExtra(BrowserActivity.URL, hotNews.url)
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