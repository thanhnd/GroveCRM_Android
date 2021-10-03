package com.grove.crm.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    val itemID: String? = null,
    val itemName: String? = null,
    val itemImage: String? = null,
    val priceTaxNotDiscount: Long? = null,
    val priceTax: Long? = null,
    val countryName: String? = null,
    val brandName: String? = null,
    val weightName: String? = null,
    val catalogName: String? = null,
    val description: String? = null
) : Parcelable
