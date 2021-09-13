
package com.grove.crm.ui.notify

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Notify
import com.grove.crm.data.model.NotifyItem
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

internal class NotifySection(
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_notify)
        .build()
) {
    var notify: Notify? = null

    override fun getContentItemsTotal(): Int {
        return notify?.listItem?.size ?: 0
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder
        notify?.listItem?.get(position)?.apply {
            itemHolder.tvNotifyTitle.text = title
            Glide
                .with(holder.itemView.context)
                .load(imageUrl)
                .centerCrop()
                .into(itemHolder.ivNotifyImage)
            itemHolder.rootView.setOnClickListener { v: View? ->
                clickListener.onItemRootViewClicked(
                    this
                )
            }
        }

    }

    override fun getLoadingViewHolder(view: View): RecyclerView.ViewHolder {
        return EmptyViewHolder(view)
    }

    internal class ItemViewHolder(rootView: View) : RecyclerView.ViewHolder(
        rootView
    ) {
        val tvNotifyTitle: TextView = rootView.findViewById(R.id.tvNotifyTitle)
        val ivNotifyImage: ImageView = rootView.findViewById(R.id.ivNotifyImage)
        val rootView: View = rootView.findViewById(R.id.rootView)

    }

    internal interface ClickListener {
        fun onItemRootViewClicked(notify: NotifyItem)
    }
}

