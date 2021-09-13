package com.grove.crm.data.model

data class Language (
    var code: String,
    var name: String
) {
    override fun toString(): String {
        return name
    }
}