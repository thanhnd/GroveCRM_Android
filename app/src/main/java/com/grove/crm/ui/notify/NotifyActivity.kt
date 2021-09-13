package com.grove.crm.ui.notify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grove.crm.R
import com.grove.crm.data.model.NotifyItem
import com.grove.crm.ui.browser.BrowserActivity
import com.grove.crm.utils.BaseActivity
import com.grove.crm.utils.CustomProgressDialog
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_notify.*

class NotifyActivity : BaseActivity() {
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