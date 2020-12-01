package com.quynhlamryan.crm.data.model


data class Article (
    val id: Long? = null,
    val title: String? = null,
    val url: String? = null,
    val imageUrl: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
    val publishDate: String? = null,
    val typeAction: String? = null,
    val isReaded: Boolean = false
)

