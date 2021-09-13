package com.grove.crm.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grove.crm.R
import com.grove.crm.data.model.Account
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

internal class AccountSection(
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_account)
        .build()
) {
    private var account: Account? = null
    fun setAccount(account: Account) {
        this.account = account
    }

    override fun getContentItemsTotal(): Int {
        return if (account != null)  1 else  0 
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return AccountSection.ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as AccountSection.ItemViewHolder
        val account = account ?: return
        itemHolder.tvAccountName.text = account.fullName
        itemHolder.tvAccountId.text = account.userName
        itemHolder.tvAccountType.text = account.crmLevelName

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

