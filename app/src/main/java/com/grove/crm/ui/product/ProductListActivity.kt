package com.grove.crm.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_product.*

class ProductListActivity : AppCompatActivity() {

    lateinit var productListViewModel: ProductListActivityViewModel
    private val adapter = ProductListAdapter()
    private var stores: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
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
}