package com.quynhlamryan.crm.ui.notify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.NotifyItem
import com.quynhlamryan.crm.ui.browser.BrowserActivity
import com.quynhlamryan.crm.utils.CustomProgressDialog
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_notify.*

class NotifyActivity : AppCompatActivity() {
    lateinit var notifyViewModel: NotifyViewModel
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private var notifySection = NotifySection(object: NotifySection.ClickListener {
        override fun onItemRootViewClicked(notify: NotifyItem) {
            openBrowser(notify)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        setTitle(R.string.notify)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        notifyViewModel = ViewModelProvider(this).get(NotifyViewModel::class.java)
        sectionAdapter = SectionedRecyclerViewAdapter()
        sectionAdapter.addSection(notifySection)
        rvNotify.adapter = sectionAdapter

    }

    override fun onStart() {
        super.onStart()

        CustomProgressDialog.showProgressDialog(this)
        notifyViewModel.getListNotify()?.observe(this, Observer { notify ->
            CustomProgressDialog.dismissProgressDialog()

            notifySection.notify = notify
            sectionAdapter.notifyDataSetChanged()
            tvEmpty.visibility = View.GONE
            rvNotify.visibility = View.VISIBLE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openBrowser(notify: NotifyItem) {
        Intent(this, BrowserActivity::class.java)
            .apply {
                putExtra(BrowserActivity.TITLE, notify.title)
                putExtra(BrowserActivity.CONTENT, notify.content)
                startActivity(this)
            }
    }
}