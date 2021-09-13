package com.grove.crm.data.model

import com.google.gson.annotations.SerializedName


data class Config(
    @SerializedName("Support")
    var support: String? = null,
    @SerializedName("TermOfUse")
    var termOfUse: String? = null,
    @SerializedName("Hotline")
    var hotline: String? = null,
    @SerializedName("AboutUs")
    var aboutUs: String? = null)

