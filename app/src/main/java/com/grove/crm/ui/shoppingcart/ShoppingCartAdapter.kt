package com.grove.crm.ui.shoppingcart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.format
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_shopping_cart.*

class ShoppingCartAdapter() :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    var products: HashMap<Product, Int> = HashMap()
        set(value) {
            field = value
            notifyDataSetChanged()

            array = value.entries.map { Pair(it.key, it.value) }
                .toTypedArray()

        }

    lateinit var array: Array<Pair<Product, Int>>

    var onItemClick: ((Product) -> Unit)? = null

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(product: Product, qty: Int) {
            tvItemName.text = product.itemName

            tvQtyAndPrice.text = "$qty x ${product.priceTax?.format()}"

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
            .inflate(R.layout.item_shopping_cart, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        array[position].also { pair ->
            viewHolder.bind(pair.first, pair.second)
            viewHolder.itemView.setOnClickListener {
                onItemClick?.invoke(pair.first)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products.size

}
