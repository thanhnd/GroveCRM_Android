package com.grove.crm.data.model

import com.google.gson.annotations.SerializedName


data class Notify(
    @SerializedName("tongDiem")
    var total: String? = null,
    var listItem: List<NotifyItem>? = listOf()
)

data class NotifyItem(
    var id: Long? = null,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("content")
    var content: String? = null,
    @SerializedName("publishDate")
    var publishDate: String? = null,
    @SerializedName("typeMessage")
    var typeMessage: String? = null,
    @SerializedName("dataID")
    var dataId: Long? = null,
    @SerializedName("isReaded")
    var isRead: Boolean = false
)

