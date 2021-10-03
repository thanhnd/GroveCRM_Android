package com.grove.crm.data.model

data class Product (
    val itemID: String? = null,
    val itemName: String? = null,
    val itemImage: String? = null,
    val priceTaxNotDiscount: Long? = null,
    val priceTax: String? = null,
    val countryName: String? = null,
    val brandName: String? = null,
    val weightName: String? = null,
    val catalogName: Boolean = false,
    val description: String? = null
)
