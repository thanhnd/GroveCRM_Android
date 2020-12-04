package com.quynhlamryan.crm.ui.mapstore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.quynhlamryan.crm.R
import com.quynhlamryan.crm.data.model.Store
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_store.*

class StoreAdapter() :
    RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    internal var stores: List<Store>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((Store) -> Unit)? = null

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @Suppress("DEPRECATION")
        fun bind(store: Store) {
            tvStoreName.text = store.storeName
            tvStoreAddress.text = store.address

            Glide
                .with(containerView.context)
                .load(store.imageUrl)
                .centerCrop()
                .into(ivImage);
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_store, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        stores?.get(position)?.also { store ->
            viewHolder.bind(store)
            viewHolder.itemView.setOnClickListener {
                onItemClick?.invoke(store)
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = stores?.size ?: 0

}
