
package com.quynhlamryan.crm.ui.transaction

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.TransactionHistory
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

internal class TransactionSection : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_transaction)
        .headerResourceId(R.layout.section_main_header)
        .build()
) {
    var transaction: TransactionHistory? = null

    override fun getContentItemsTotal(): Int {
        return transaction?.listItem?.size ?: 0
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder
        transaction?.listItem?.get(position)?.apply {
            itemHolder.tvStore.text = store
            itemHolder.receiptNumber.text = receiptNumber
            itemHolder.tvOrderDate.text = createdDate
            itemHolder.tvAmount.text = amount
            itemHolder.tvRefund.text = refund
            itemHolder.tvSavedPoint.text = score
            itemHolder.tvUsedPoint.text = score
        }

    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.tvTitle.text = headerHolder.tvTitle.resources.getString(R.string.total_score, transaction?.totalScore ?: 0)
    }

    override fun getLoadingViewHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    internal class ItemViewHolder(rootView: View) : RecyclerView.ViewHolder(
        rootView
    ) {
        val tvStore: TextView = rootView.findViewById(R.id.tvStore)
        val receiptNumber: TextView = rootView.findViewById(R.id.tvReceiptNumber)
        val tvOrderDate: TextView = rootView.findViewById(R.id.tvOrderDate)
        val tvAmount: TextView = rootView.findViewById(R.id.tvAmount)
        val tvRefund: TextView = rootView.findViewById(R.id.tvRefund)
        val tvSavedPoint: TextView = rootView.findViewById(R.id.tvSavedPoint)
        val tvUsedPoint: TextView = rootView.findViewById(R.id.tvUsedPoint)

    }
}

