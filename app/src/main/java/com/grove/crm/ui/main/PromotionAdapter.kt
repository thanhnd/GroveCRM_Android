package com.grove.crm.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Promotion
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_promotion.*

class PromotionAdapter() :
    RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {

    var promotions: List<Promotion>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Promotion) -> Unit)? = null

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @Suppress("DEPRECATION")
        fun bind(promotion: Promotion) {
            
            Glide
                .with(containerView.context)
                .load(promotion.imageUrl)
                .centerCrop()
                .into(ivImage);
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_promotion, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        promotions?.get(position)?.also { promotion ->
            viewHolder.bind(promotion)
            viewHolder.itemView.setOnClickListener {
                onItemClick?.invoke(promotion)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = promotions?.size ?: 0

}
