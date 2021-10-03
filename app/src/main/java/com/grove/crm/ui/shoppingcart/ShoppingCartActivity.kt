package com.grove.crm.ui.shoppingcart

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.grove.crm.R
import com.grove.crm.ui.order.OrderActivity
import com.grove.crm.ui.product.ProductDetailActivity
import com.grove.crm.utils.BadgeDrawable
import com.grove.crm.utils.ShoppingCartManager
import com.grove.crm.utils.formatCurrency
import kotlinx.android.synthetic.main.activity_shopping_cart.*

class ShoppingCartActivity : AppCompatActivity() {

    private var menu: Menu? = null
    private val adapter = ShoppingCartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.grove.crm.R.layout.activity_shopping_cart)

        adapter.products = ShoppingCartManager.items
        adapter.onItemClick = { product ->
            Intent(this, ProductDetailActivity::class.java)
                .apply {
                    putExtra(ProductDetailActivity.PRODUCT, product)
                    startActivity(this)
                }
        }
        rvItem.adapter = adapter

        btnOrder.setOnClickListener {
            Intent(this, OrderActivity::class.java)
                .apply { startActivity(this) }
        }
        title = getString(R.string.title_shopping_cart)
    }

    override fun onResume() {
        super.onResume()

        menu?.findItem(com.grove.crm.R.id.action_cart)?.let {
            val count = ShoppingCartManager.countItem()
            if (count > 0) {

                setBadgeCount(it.icon as LayerDrawable, "${ShoppingCartManager.countItem()}");
            } else {
                removeBadgeCount(it.icon as LayerDrawable)
            }
        }

        tvTotalPrice.text = getString(R.string.total_price, ShoppingCartManager.totalPrice.formatCurrency())
    }

    private fun setBadgeCount(icon: LayerDrawable, count: String) {
        val badge: BadgeDrawable

        // Reuse drawable if possible
        val reuse = icon.findDrawableByLayerId(R.id.ic_badge)
        badge = if (reuse != null && reuse is BadgeDrawable) {
            reuse as BadgeDrawable
        } else {
            BadgeDrawable(this)
        }
        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_badge, badge)
    }

    private fun removeBadgeCount(icon: LayerDrawable) {
        val badge: BadgeDrawable

        // Reuse drawable if possible
        val reuse = icon.findDrawableByLayerId(R.id.ic_badge)

        badge = if (reuse != null && reuse is BadgeDrawable) {
            reuse as BadgeDrawable
        } else {
            BadgeDrawable(this)
        }
        badge.alpha = 0
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_badge, badge)
    }
}