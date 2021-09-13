package com.grove.crm.data.request

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("token")
    var token: String? = null,
    @SerializedName("osName")
    var osName: String? = null,
    @SerializedName("crmid")
    var crmId: String? = null
)

