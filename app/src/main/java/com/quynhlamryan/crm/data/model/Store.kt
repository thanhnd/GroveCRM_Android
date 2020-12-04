package com.quynhlamryan.crm.data.model

data class Store (
    var id: Long,
    var storeName: String,
    var imageUrl: String? = null,
    var address: String? = null,
    var latitude: Double,
    var longitude: Double
)