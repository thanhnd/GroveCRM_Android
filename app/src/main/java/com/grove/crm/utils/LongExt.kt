package com.grove.crm.utils

import java.text.DecimalFormat

fun Long.formatCurrency(): String {
    val dec = DecimalFormat("#,###.##")
    return "${dec.format(this)} đ"
}

fun Long.format(): String {
    val dec = DecimalFormat("#,###.##")
    return dec.format(this)
}