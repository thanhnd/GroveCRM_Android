package com.grove.crm.data.model

import com.google.gson.annotations.SerializedName




/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
class ResponseResult<T : Any> {

    @SerializedName("isSuccessed")
    var isSuccessed: Boolean = false

    @SerializedName("message")
    var message: String? = null

    @SerializedName("statusCode")
    var statusCode: Int? = null

    @SerializedName("total_pages")
    var totalPages: Int? = null

    @SerializedName("resultObj")
    var resultObj: T? = null
}