package com.grove.crm.data.model


import com.google.gson.annotations.SerializedName

data class HotNews(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("hotNewImageUrl")
    val hotNewImageUrl: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("content")
    val content: String? = null
)