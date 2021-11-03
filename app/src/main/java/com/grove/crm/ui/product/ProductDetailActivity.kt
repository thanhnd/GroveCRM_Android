package com.grove.crm.ui.product

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.ShoppingCartManager
import com.grove.crm.utils.formatCurrency
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.ivImage
import kotlinx.android.synthetic.main.item_store.*

class ProductDetailActivity : AppCompatActivity() {
    private val maxQty = 999
    private var qty = 1
        set(value) {
            field = value
            onUpdateQty()
        }
    var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        title = getString(R.string.title_product)

        intent.getParcelableExtra<Product>(PRODUCT)?.apply {
            product = this

            Glide
                .with(this@ProductDetailActivity)
                .load(itemImage)
                .centerCrop()
                .into(ivImage)
            tvItemName.text = itemName
            tvPrice.text = priceTax?.formatCurrency()
            priceTaxNotDiscount?.let {
                tvOldPrice.text = priceTaxNotDiscount.formatCurrency()
                tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvOldPrice.visibility = View.VISIBLE
            } ?: run {
                tvOldPrice.visibility = View.GONE
            }

            tvSKU.text = itemID
            tvOriginal.text = countryName
            tvBranch.text = brandName
            tvUnit.text = weightName
            tvCatalog.text = catalogName
            tvDescription.text = description

            ShoppingCartManager.items.get(product)?.let {
                qty = it
            }
        }

        if (product == null) {
            finish()
        }

        onUpdateQty()

        btnIncreaseQty.setOnClickListener {
            if (qty >= maxQty) {
                qty = maxQty
            } else {
                qty += 1
            }
        }

        btnDecreaseQty.setOnClickListener {
            if (qty <= 0) {
                qty = 0
            } else {
                qty -= 1
            }
        }

        btnAddToCart.setOnClickListener {
            if (product == null) return@setOnClickListener
            if (qty > 0) {
                ShoppingCartManager.items[product!!] = qty
            } else {
                ShoppingCartManager.items.remove(product!!)
            }

            finish()
        }
    }

    private fun onUpdateQty() {
        tvQty.text = "$qty"
        product?.priceTax?.let { price ->
            tvTotalPrice.text = (qty * price).formatCurrency()
        }
        if (qty == 0) {
            ShoppingCartManager.items[product]?.let {
                btnAddToCart.setText(R.string.remove_from_cart)
            } ?: run {
                btnAddToCart.isEnabled = false
            }
        } else {
            btnAddToCart.isEnabled = true
            btnAddToCart.setText(R.string.add_to_cart)
        }
    }

    companion object {
        const val PRODUCT = "product"
    }
}