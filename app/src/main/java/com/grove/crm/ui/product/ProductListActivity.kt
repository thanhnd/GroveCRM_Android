package com.grove.crm.ui.product

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.BadgeDrawable
import com.grove.crm.utils.CustomProgressDialog
import com.grove.crm.utils.ShoppingCartManager
import kotlinx.android.synthetic.main.activity_product.*




class ProductListActivity : AppCompatActivity() {

    private var menu: Menu? = null
    lateinit var productListViewModel: ProductListActivityViewModel
    private val adapter = ProductListAdapter()
    private var stores: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.grove.crm.R.layout.activity_product)
        productListViewModel = ViewModelProvider(this).get(ProductListActivityViewModel::class.java)
        adapter.onItemClick = { product ->
            Intent(this, ProductDetailActivity::class.java)
                .apply {
                    putExtra(ProductDetailActivity.PRODUCT, product)
                    startActivity(this)
                }
        }
        rvProduct.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        CustomProgressDialog.showProgressDialog(this)
        productListViewModel.getProducts()?.observe(this, Observer { stores ->
            CustomProgressDialog.dismissProgressDialog()
            adapter.products = stores

            this.stores = stores
        })
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_shopping, menu)
        this.menu = menu

        return true
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