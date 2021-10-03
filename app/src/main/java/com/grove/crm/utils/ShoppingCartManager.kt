package com.grove.crm.utils

import com.grove.crm.data.model.Product

object ShoppingCartManager {

    var items: HashMap<Product, Int> = HashMap()

    fun getTotalPrice() : Long {
        var value = 0L
        items.forEach { entry ->
            val product = entry.key
            val qty = entry.value
            product.priceTax?.let { price ->
                value += price * qty
            }

        }
        return value
    }

    fun countItem(): Int {
        var value = 0
        items.forEach { entry ->

            val qty = entry.value
            value += qty
        }
        return value
    }

    fun clear() {
        items = HashMap()
    }

}