package com.grove.crm.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.grove.crm.R
import com.grove.crm.data.model.Product
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.activity_product_detail.ivImage
import kotlinx.android.synthetic.main.item_store.*

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        intent.getParcelableExtra<Product>(PRODUCT)?.apply {
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
            tvCatalog.text = catalogName
            tvDescription.text = description
        }
    }

    companion object {
        const val PRODUCT = "product"
    }
}