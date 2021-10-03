package com.grove.crm.data.model

data class Order(
    var listItem: List<ProductItem>,
    var deliveryInformation: DeliveryInformation,
    var eInvoiceInformation: EInvoiceInformation,
)

data class DeliveryInformation(
    var deliveryAddress: String,
    var deliveryPerson: String,
    var deliveryMobile: String,
    var deliveryNote: String
)

data class EInvoiceInformation(
    var companyName: String,
    var companyAddress: String,
    var companyTaxCode: String
)

data class ProductItem(
    var itemID: String,
    var price: Long,
    var qty: Int
) {
    constructor(product: Product, qty: Int) : this(product.itemID ?: "", product.priceTax ?: 0, qty)
}

