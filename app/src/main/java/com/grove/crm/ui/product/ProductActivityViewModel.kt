package com.grove.crm.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grove.crm.data.ProductActivityRepository
import com.grove.crm.data.model.Product

class ProductActivityViewModel : ViewModel() {

    private var ldProducts: MutableLiveData<List<Product>>? = null

    fun getProducts(): LiveData<List<Product>>? {
        this.ldProducts = ProductActivityRepository.getListProducts()
        return this.ldProducts
    }
}