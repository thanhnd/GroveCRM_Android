package com.grove.crm.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grove.crm.R
import com.grove.crm.data.model.Product
import com.grove.crm.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {

    lateinit var productViewModel: ProductActivityViewModel
    private val adapter = ProductAdapter()
    private var stores: List<Product> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        productViewModel = ViewModelProvider(this).get(ProductActivityViewModel::class.java)

        rvProduct.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        CustomProgressDialog.showProgressDialog(this)
        productViewModel.getProducts()?.observe(this, Observer { stores ->
            CustomProgressDialog.dismissProgressDialog()
            adapter.products = stores

            this.stores = stores
        })
    }
}