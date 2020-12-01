package com.quynhlamryan.crm.data.model

import com.google.gson.annotations.SerializedName


class TransactionHistory {
    @SerializedName("tongDiem")
    var totalScore: String? = null
    var listItem: List<TransactionItem>? = null
}

class TransactionItem {
    var id: Long? = null
    @SerializedName("noiMuaHang")
    var store: String? = null
    @SerializedName("soHoaDon")
    var receiptNumber: String? = null
    @SerializedName("ngayHoaDon")
    var createdDate: String? = null
    @SerializedName("tongTienMuaHang")
    var amount: String? = null
    @SerializedName("traHang")
    var refund: String? = null
    @SerializedName("diemTichLuy")
    var score: String? = null
    @SerializedName("diemSuDung")
    var usedScore: String? = null
}

