package com.quynhlamryan.crm.ui.main

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Promotion
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder


internal class PromotionSection(
    private val title: String,
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_section_promotion)
        .headerResourceId(R.layout.section_main_header)
        .build()
) {
    private val list: MutableList<Promotion> = arrayListOf()
    fun setList(list: List<Promotion>?) {
        val list = list ?: return
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getContentItemsTotal(): Int {
        return if (list.isEmpty())  0 else 1
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return PromotionSection.ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as PromotionSection.ItemViewHolder
        val context = itemHolder.rootView.context
        val promotionAdapter = PromotionAdapter()
        promotionAdapter.promotions = list
        promotionAdapter.onItemClick = {
            clickListener.onItemRootViewClicked(this, it)
        }
        itemHolder.rvPromotion.apply {
            adapter = promotionAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.tvTitle.text = title
    }

    override fun getLoadingViewHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    internal interface ClickListener {
        fun onItemRootViewClicked(section: PromotionSection?, promotion: Promotion)
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    }

    internal class ItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(
        rootView
    ) {
        val rvPromotion: RecyclerView = rootView.findViewById(R.id.rvPromotion)
    }
}

