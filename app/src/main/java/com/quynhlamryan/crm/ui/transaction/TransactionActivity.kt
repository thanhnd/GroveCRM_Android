package com.quynhlamryan.crm.ui.transaction

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.utils.CustomProgressDialog
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {
    lateinit var transactionViewModel: TransactionViewModel
    private lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private var transactionSection = TransactionSection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        setTitle(R.string.transaction_history)

        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        sectionAdapter = SectionedRecyclerViewAdapter()
        sectionAdapter.addSection(transactionSection)
        rvTransaction.adapter = sectionAdapter

    }

    override fun onStart() {
        super.onStart()

        CustomProgressDialog.showProgressDialog(this)
        transactionViewModel.getListTransactions()?.observe(this, Observer { transaction ->
            CustomProgressDialog.dismissProgressDialog()

            transactionSection.transaction = transaction
            sectionAdapter.notifyDataSetChanged()
            tvEmpty.visibility = View.GONE
            rvTransaction.visibility = View.VISIBLE
        })
    }
}