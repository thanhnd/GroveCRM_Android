package com.quynhlamryan.crm.data.model


class Inbox {
    var total: Int? = null
    var listItem: List<InboxItem>? = null
}

class InboxItem {
    var id: Long? = null
    var title: String? = null
    var imageUrl: String? = null
    var content: String? = null
    var publishDate: String? = null
    var typeMessage: String? = null
    var isReaded: Boolean = false
}

