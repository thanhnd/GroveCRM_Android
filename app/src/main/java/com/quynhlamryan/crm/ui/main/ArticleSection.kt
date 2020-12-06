package com.quynhlamryan.crm.ui.main

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Article
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

internal class ArticleSection(
    private val title: String,
    private val clickListener: ClickListener
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.item_article)
        .headerResourceId(R.layout.section_main_header)
        .build()
) {
    private val list: MutableList<Article> = arrayListOf()
    fun setList(list: List<Article>?) {
        val list = list ?: return
        this.list.clear()
        this.list.addAll(list)
    }

    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder
        val article = list[position]
        itemHolder.tvTitle.text = article.title
        itemHolder.tvDescription.text = article.content
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            itemHolder.tvDescription.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
        } else {
            itemHolder.tvDescription.text = Html.fromHtml(article.content)
        }
        Glide
            .with(itemHolder.rootView.context)
            .load(article.imageUrl)
            .centerCrop()
            .into(itemHolder.ivImage)
        itemHolder.rootView.setOnClickListener { v: View? ->
            clickListener.onItemRootViewClicked(
                this,
                article
            )
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
        fun onItemRootViewClicked(section: ArticleSection?, article: Article)
    }

    internal class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

    }

    internal class ItemViewHolder(val rootView: View) : RecyclerView.ViewHolder(
        rootView
    ) {
        val ivImage: ImageView = rootView.findViewById(R.id.ivArticleImage)
        val tvTitle: TextView = rootView.findViewById(R.id.tvArticleTitle)
        val tvDescription: TextView = rootView.findViewById(R.id.tvArticleDescription)

    }
}

