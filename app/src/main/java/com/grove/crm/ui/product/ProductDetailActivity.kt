package com.grove.crm.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.ShoppingCartManager
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.ivImage
import kotlinx.android.synthetic.main.item_store.*

class ProductDetailActivity : AppCompatActivity() {
    val MaxQty = 999
    var qty = 1
    var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        intent.getParcelableExtra<Product>(PRODUCT)?.apply {
            product = this

            Glide
                .with(this@ProductDetailActivity)
                .load(itemImage)
                .centerCrop()
                .into(ivImage)
            tvItemName.text = itemName
            tvPrice.text = "$priceTax"
            tvOldPrice.text = "$priceTaxNotDiscount"
            tvSKU.text = itemID
            tvOriginal.text = countryName
            tvBranch.text = brandName
            tvUnit.text = weightName
            tvCatalog.text = catalogName
            tvDescription.text = description

        }

        btnIncreaseQty.setOnClickListener {
            if (qty >= MaxQty) {
                qty = MaxQty
            } else {
                qty += 1
            }

            onUpdateQty()

        }

        btnDecreaseQty.setOnClickListener {
            if (qty <= 0) {
                qty = 0
            } else {
                qty -= 1
            }

            onUpdateQty()
        }

        btnAddToCart.setOnClickListener {
            if (product == null) return@setOnClickListener

            ShoppingCartManager.items[product!!] = qty
        }
    }

    private fun onUpdateQty() {
        tvQty.text = "$qty"
        product?.priceTax?.let { price ->
            tvTotalPrice. text = "${qty * price}"
        }
    }

    companion object {
        const val PRODUCT = "product"
    }
}