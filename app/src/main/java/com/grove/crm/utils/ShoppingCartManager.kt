package com.grove.crm.utils

import com.grove.crm.data.model.Product
import com.grove.crm.data.model.ProductItem

object ShoppingCartManager {

    var items: HashMap<Product, Int> = HashMap()

    var totalPrice = 0L
        get() {
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

    var listItem: List<ProductItem> = listOf()
        get() {
            return items.map { ProductItem(it.key, it.value) }
        }


    fun clear() {
        items.clear()
    }
}