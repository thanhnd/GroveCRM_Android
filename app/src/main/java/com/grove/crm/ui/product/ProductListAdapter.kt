package com.grove.crm.ui.product

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.formatCurrency
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_product.*

class ProductListAdapter() :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    internal var products: List<Product>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Product) -> Unit)? = null

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @Suppress("DEPRECATION")
        fun bind(product: Product) {
            tvItemName.text = product.itemName
            tvPrice.text = product.priceTax?.formatCurrency()
            product.priceTaxNotDiscount?.let {
                tvOldPrice.text = it.formatCurrency()
                tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvOldPrice.visibility = View.VISIBLE
            } ?: run {
                tvOldPrice.visibility = View.GONE
            }

            Glide
                .with(containerView.context)
                .load(product.itemImage)
                .centerCrop()
                .into(ivImage)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_product, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        products?.get(position)?.also { product ->
            viewHolder.bind(product)
            viewHolder.itemView.setOnClickListener {
                onItemClick?.invoke(product)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products?.size ?: 0

}
