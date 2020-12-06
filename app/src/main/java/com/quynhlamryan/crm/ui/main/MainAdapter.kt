package com.quynhlamryan.crm.ui.main

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Article
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_article.*

class MainAdapter() :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    internal var articles: List<Article>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Article) -> Unit)? = null

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @Suppress("DEPRECATION")
        fun bind(article: Article) {
            tvArticleTitle.text = article.title
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvArticleDescription.text = Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY)
            } else {
                tvArticleDescription.text = Html.fromHtml(article.content)
            }
            Glide
                .with(containerView.context)
                .load(article.imageUrl)
                .centerCrop()
                .into(ivArticleImage);
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_article, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        articles?.get(position)?.also { article ->
            viewHolder.bind(article)
            viewHolder.itemView.setOnClickListener {
                onItemClick?.invoke(article)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = articles?.size ?: 0

}
