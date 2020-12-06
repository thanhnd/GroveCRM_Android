package com.quynhlamryan.crm.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Account
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder
import java.util.*

internal class AccountSection(
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_account)
        .build()
) {
    private val list: MutableList<Account>
    fun setList(list: Account) {
        this.list.clear()
        this.list.add(list)
    }

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return AccountSection.ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as AccountSection.ItemViewHolder
        val account = list[position]
        itemHolder.tvAccountName.text = account.fullName
        itemHolder.tvAccountId.text = account.userName
        itemHolder.tvAccountType.text = account.typeMember

        itemHolder.btnMemberCard.setOnClickListener {
            clickListener.onMemberCardClicked()
        }

        itemHolder.btnTransactionHistory.setOnClickListener {
            clickListener.onTransactionHistoryClicked()
        }
    }

    override fun getLoadingViewHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    internal interface ClickListener {
        fun onMemberCardClicked()
        fun onTransactionHistoryClicked()
    }

    init {
        list = ArrayList()
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    }

    internal class ItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(
        rootView
    ) {
        val tvAccountName: TextView = rootView.findViewById(R.id.tvAccountName)
        val tvAccountId: TextView = rootView.findViewById(R.id.tvAccountId)
        val tvAccountType: TextView = rootView.findViewById(R.id.tvAccountType)
        val btnMemberCard: TextView = rootView.findViewById(R.id.btnMemberCard)
        val btnTransactionHistory: TextView = rootView.findViewById(R.id.btnTransactionHistory)

    }
}

